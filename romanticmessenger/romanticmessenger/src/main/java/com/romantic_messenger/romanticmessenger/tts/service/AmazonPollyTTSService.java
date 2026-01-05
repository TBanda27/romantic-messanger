package com.romantic_messenger.romanticmessenger.tts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.*;

import java.io.IOException;

@Service
@Slf4j
public class AmazonPollyTTSService {
    private final PollyClient pollyClient;

    public AmazonPollyTTSService(PollyClient pollyClient) {
        this.pollyClient = pollyClient;
    }

    public byte[] convertTextToSpeech(String text) throws IOException {
        log.info("Calling Amazon Polly API to convert text to speech");

        SynthesizeSpeechRequest request = SynthesizeSpeechRequest.builder()
                .text(text)
                .voiceId(VoiceId.JOANNA)
                .outputFormat(OutputFormat.MP3)
                .engine(Engine.NEURAL)
                .build();

        ResponseInputStream<SynthesizeSpeechResponse> responseInputStream = pollyClient.synthesizeSpeech(request);
        byte[] audioBytes = responseInputStream.readAllBytes();
        log.info("Received audio data from Amazon Polly {} bytes", audioBytes.length);
        return audioBytes;
    }
}
