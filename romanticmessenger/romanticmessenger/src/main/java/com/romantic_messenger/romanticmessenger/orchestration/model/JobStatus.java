package com.romantic_messenger.romanticmessenger.orchestration.model;

import com.romantic_messenger.romanticmessenger.orchestration.dto.JobStatusResponse;
import com.romantic_messenger.romanticmessenger.orchestration.enums.PipelineStage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobStatus {
    private final String jobId;
    private final String phoneNumber;
    private final String bookTheme;

    private volatile PipelineStage currentStage;
    private volatile String romanticMessage;
    private volatile byte[] audioBytes;
    private volatile String audioUrl;
    private volatile String errorMessage;
    private volatile PipelineStage errorStage;

    private final LocalDateTime startedAt;
    private volatile LocalDateTime lastUpdatedAt;
    private volatile LocalDateTime completedAt;

    public JobStatus(String jobId, String phoneNumber, String bookTheme) {
        this.jobId = jobId;
        this.phoneNumber = phoneNumber;
        this.bookTheme = bookTheme;
        this.currentStage = PipelineStage.REQUESTED;
        this.startedAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public synchronized void updateStage(PipelineStage newStage) {
        this.currentStage = newStage;
        this.lastUpdatedAt = LocalDateTime.now();

        if (newStage.isTerminal()) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public synchronized void markFailed(PipelineStage failedStage, String errorMessage) {
        this.currentStage = PipelineStage.FAILED;
        this.errorStage = failedStage;
        this.errorMessage = errorMessage;
        this.lastUpdatedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public JobStatusResponse toResponse() {
        return new JobStatusResponse(
            jobId,
            currentStage,
            currentStage.getProgressPercentage(),
            currentStage.getDescription(),
            startedAt,
            lastUpdatedAt,
            completedAt,
            romanticMessage,
            audioUrl,
            phoneNumber,
            errorMessage,
            errorStage != null ? errorStage.name() : null
        );
    }
}
