package com.romantic_messenger.romanticmessenger.tts.controller;

import com.romantic_messenger.romanticmessenger.tts.service.AmazonPollyTTSService;
import com.romantic_messenger.romanticmessenger.tts.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/tts")
@Slf4j
public class AmazonPollyTTSController {
    private final AmazonPollyTTSService amazonPollyTTSService;
    private final S3Service s3Service;

    public AmazonPollyTTSController(AmazonPollyTTSService amazonPollyTTSService, S3Service s3Service) {
        this.amazonPollyTTSService = amazonPollyTTSService;
        this.s3Service = s3Service;
    }

    @PostMapping
    public ResponseEntity<String> convertTextToSpeech(@RequestBody String text) throws IOException {
        log.info("Converting text to Speech for text: {}", text);

        byte[] audioBytes = amazonPollyTTSService.convertTextToSpeech(text);
        String publicUrl = s3Service.uploadAudioFile(audioBytes);

        log.info("Processing completed. Audio file URL: {}", publicUrl);
        return ResponseEntity.ok(publicUrl);
    }
}
