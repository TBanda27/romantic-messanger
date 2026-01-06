package com.romantic_messenger.romanticmessenger.orchestration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.romantic_messenger.romanticmessenger.orchestration.enums.PipelineStage;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record JobStatusResponse(
    String jobId,
    PipelineStage currentStage,
    int progressPercentage,
    String stageDescription,
    LocalDateTime startedAt,
    LocalDateTime lastUpdatedAt,
    LocalDateTime completedAt,

    // Populated on success
    String romanticMessage,
    String audioUrl,
    String recipientPhoneNumber,

    // Populated on failure
    String errorMessage,
    String errorStage
) {
    public boolean isComplete() {
        return currentStage != null && currentStage.isTerminal();
    }

    public boolean isFailed() {
        return currentStage == PipelineStage.FAILED;
    }
}
