package com.romantic_messenger.romanticmessenger.orchestration.enums;

import lombok.Getter;

@Getter
public enum PipelineStage {
    REQUESTED("Job requested, queued for processing"),
    GENERATING_MESSAGE("Generating romantic message via Claude AI"),
    MESSAGE_GENERATED("Romantic message generated successfully"),
    CONVERTING_TO_AUDIO("Converting message to speech via Amazon Polly"),
    AUDIO_CONVERTED("Audio conversion completed"),
    UPLOADING_TO_S3("Uploading audio file to S3 storage"),
    UPLOADED("Audio file uploaded successfully"),
    SENDING_MMS("Sending MMS via Twilio"),
    COMPLETED("Message sent successfully"),
    FAILED("Pipeline failed");

    private final String description;

    PipelineStage(String description) {
        this.description = description;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED;
    }

    public int getProgressPercentage() {
        return switch (this) {
            case REQUESTED, FAILED -> 0;
            case GENERATING_MESSAGE -> 10;
            case MESSAGE_GENERATED -> 25;
            case CONVERTING_TO_AUDIO -> 40;
            case AUDIO_CONVERTED -> 55;
            case UPLOADING_TO_S3 -> 70;
            case UPLOADED -> 85;
            case SENDING_MMS -> 90;
            case COMPLETED -> 100;
        };
    }
}
