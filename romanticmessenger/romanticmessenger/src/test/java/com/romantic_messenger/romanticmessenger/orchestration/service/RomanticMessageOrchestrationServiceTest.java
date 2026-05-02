package com.romantic_messenger.romanticmessenger.orchestration.service;

import com.romantic_messenger.romanticmessenger.claude.service.AmazonPollyTTSService;
import com.romantic_messenger.romanticmessenger.claude.service.RomanticMessageService;
import com.romantic_messenger.romanticmessenger.claude.service.S3Service;
import com.romantic_messenger.romanticmessenger.claude.service.TwilioService;
import com.romantic_messenger.romanticmessenger.orchestration.enums.PipelineStage;
import com.romantic_messenger.romanticmessenger.orchestration.exception.JobNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Unit-test the orchestration service with all external services mocked.
//       These tests should run without a Spring context — use MockitoExtension only.
@ExtendWith(MockitoExtension.class)
class RomanticMessageOrchestrationServiceTest {

    @Mock
    private RomanticMessageService romanticMessageService;
    @Mock
    private AmazonPollyTTSService pollyService;
    @Mock
    private S3Service s3Service;
    @Mock
    private TwilioService twilioService;

    @InjectMocks
    private RomanticMessageOrchestrationService orchestrationService;

    // TODO: initiateMessagePipeline should store the job and return a non-null UUID string.
    @Test
    void initiateMessagePipeline_returnsJobId() {
        // TODO: implement
    }

    // TODO: getJobStatus with a valid jobId should return a response whose currentStage
    //       equals PipelineStage.REQUESTED immediately after job creation.
    @Test
    void getJobStatus_afterCreation_stageIsRequested() {
        // TODO: implement
    }

    // TODO: getJobStatus with an unknown jobId should throw JobNotFoundException.
    @Test
    void getJobStatus_unknownId_throwsJobNotFoundException() {
        // TODO: implement — expect JobNotFoundException
    }

    // TODO: executePipelineAsync happy path: all mocks return valid data, final stage
    //       should be COMPLETED and romanticMessage / audioUrl should be populated.
    //       Note: @Async is disabled in tests by default; call executePipelineAsync directly.
    @Test
    void executePipelineAsync_happyPath_jobReachesCompleted() {
        // TODO: implement
    }

    // TODO: executePipelineAsync when RomanticMessageService throws, the job should
    //       transition to FAILED and the error message should be preserved.
    @Test
    void executePipelineAsync_claudeFailure_jobMarkedFailed() {
        // TODO: implement
    }

    // TODO: executePipelineAsync when Polly throws, the job should be FAILED
    //       and polly / s3 / twilio should not be called after the failure.
    @Test
    void executePipelineAsync_pollyFailure_jobMarkedFailedAndS3NotCalled() {
        // TODO: implement
    }

    // TODO: cleanupOldJobs should remove completed jobs older than 1 hour and leave
    //       recent jobs untouched. Requires injecting a test clock or manipulating
    //       JobStatus.completedAt via reflection / a test constructor.
    @Test
    void cleanupOldJobs_removesStaleJobsOnly() {
        // TODO: implement
    }
}
