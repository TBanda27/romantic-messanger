package com.romantic_messenger.romanticmessenger.orchestration.dto;

import com.romantic_messenger.romanticmessenger.orchestration.enums.PipelineStage;

import java.time.LocalDateTime;

public record RomanticMessageResponse(
    String jobId,
    PipelineStage currentStage,
    String message,
    LocalDateTime timestamp,
    String statusEndpoint
) {
    public static RomanticMessageResponse created(String jobId) {
        return new RomanticMessageResponse(
            jobId,
            PipelineStage.REQUESTED,
            "Job created successfully. Use the status endpoint to poll for progress.",
            LocalDateTime.now(),
            "/api/v1/messages/status/" + jobId
        );
    }
}
