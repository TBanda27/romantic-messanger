package com.romantic_messenger.romanticmessenger.orchestration.service;

import com.romantic_messenger.romanticmessenger.claude.dto.MessageRequestDTO;
import com.romantic_messenger.romanticmessenger.claude.service.AmazonPollyTTSService;
import com.romantic_messenger.romanticmessenger.claude.service.RomanticMessageService;
import com.romantic_messenger.romanticmessenger.claude.service.S3Service;
import com.romantic_messenger.romanticmessenger.claude.service.TwilioService;
import com.romantic_messenger.romanticmessenger.orchestration.dto.JobStatusResponse;
import com.romantic_messenger.romanticmessenger.orchestration.dto.RomanticMessageRequest;
import com.romantic_messenger.romanticmessenger.orchestration.enums.PipelineStage;
import com.romantic_messenger.romanticmessenger.orchestration.exception.JobNotFoundException;
import com.romantic_messenger.romanticmessenger.orchestration.model.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RomanticMessageOrchestrationService {

    private final Map<String, JobStatus> jobStore = new ConcurrentHashMap<>();

    private final RomanticMessageService romanticMessageService;
    private final AmazonPollyTTSService pollyService;
    private final S3Service s3Service;
    private final TwilioService twilioService;

    public RomanticMessageOrchestrationService(
            RomanticMessageService romanticMessageService,
            AmazonPollyTTSService pollyService,
            S3Service s3Service,
            TwilioService twilioService) {
        this.romanticMessageService = romanticMessageService;
        this.pollyService = pollyService;
        this.s3Service = s3Service;
        this.twilioService = twilioService;
    }

    public String initiateMessagePipeline(RomanticMessageRequest request) {
        String jobId = UUID.randomUUID().toString();
        JobStatus job = new JobStatus(jobId, request.phoneNumber(), request.bookTheme());

        jobStore.put(jobId, job);
        log.info("Job {} created for phone: {}", jobId, request.phoneNumber());

        executePipelineAsync(job);

        return jobId;
    }

    public JobStatusResponse getJobStatus(String jobId) {
        JobStatus job = jobStore.get(jobId);
        if (job == null) {
            throw new JobNotFoundException("Job not found: " + jobId);
        }
        return job.toResponse();
    }

    @Async("romanticMessageExecutor")
    public void executePipelineAsync(JobStatus job) {
        try {
            job.updateStage(PipelineStage.GENERATING_MESSAGE);
            String message = generateMessage(job);
            job.setRomanticMessage(message);
            job.updateStage(PipelineStage.MESSAGE_GENERATED);

            job.updateStage(PipelineStage.CONVERTING_TO_AUDIO);
            byte[] audioBytes = convertToAudio(message);
            job.setAudioBytes(audioBytes);
            job.updateStage(PipelineStage.AUDIO_CONVERTED);

            job.updateStage(PipelineStage.UPLOADING_TO_S3);
            String audioUrl = uploadToS3(audioBytes);
            job.setAudioUrl(audioUrl);
            job.updateStage(PipelineStage.UPLOADED);

            job.updateStage(PipelineStage.SENDING_MMS);
            sendMMS(job.getPhoneNumber(), audioUrl);
            job.updateStage(PipelineStage.COMPLETED);

            log.info("Job {} completed successfully", job.getJobId());

        } catch (Exception e) {
            log.error("Job {} failed at stage {}: {}",
                job.getJobId(), job.getCurrentStage(), e.getMessage(), e);
            job.markFailed(job.getCurrentStage(), e.getMessage());
        }
    }

    private String generateMessage(JobStatus job) {
        MessageRequestDTO request = new MessageRequestDTO(job.getBookTheme());
        return romanticMessageService.createRomanticMessage(request).romanticMessage();
    }

    private byte[] convertToAudio(String message) throws Exception {
        return pollyService.convertTextToSpeech(message);
    }

    private String uploadToS3(byte[] audioBytes) {
        return s3Service.uploadAudioFile(audioBytes);
    }

    private void sendMMS(String phoneNumber, String audioUrl) {
        twilioService.sendMMS(phoneNumber, audioUrl);
    }

    @Scheduled(fixedRate = 1800000)
    public void cleanupOldJobs() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        int removed = 0;

        for (Map.Entry<String, JobStatus> entry : jobStore.entrySet()) {
            JobStatus job = entry.getValue();
            if (job.getCompletedAt() != null && job.getCompletedAt().isBefore(cutoff)) {
                jobStore.remove(entry.getKey());
                removed++;
            }
        }

        if (removed > 0) {
            log.info("Cleaned up {} completed jobs older than 1 hour", removed);
        }
    }
}
