package com.romantic_messenger.romanticmessenger.tts.controller;

import com.romantic_messenger.romanticmessenger.tts.service.AmazonPollyTTSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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


    public AmazonPollyTTSController(AmazonPollyTTSService amazonPollyTTSService) {
        this.amazonPollyTTSService = amazonPollyTTSService;
    }

    @PostMapping
    public ResponseEntity<byte[]> convertTextToSpeech(@RequestBody String text) throws IOException {
        log.info("Converting text to Speech");

        byte[] audioBytes = amazonPollyTTSService.convertTextToSpeech(text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.set("Content-Disposition", "attachment; filename=audio.mp3");

        log.info("Convertion Successful");
        return ResponseEntity.ok()
                .headers(headers)
                .body(audioBytes);
    }

}
