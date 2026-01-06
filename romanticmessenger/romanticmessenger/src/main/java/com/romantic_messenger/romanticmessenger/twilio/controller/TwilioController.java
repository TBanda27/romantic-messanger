package com.romantic_messenger.romanticmessenger.twilio.controller;

import com.romantic_messenger.romanticmessenger.twilio.service.TwilioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/twilio")
@Slf4j
public class TwilioController {
    private final TwilioService twilioService;

    public TwilioController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping("/send-mms")
    public ResponseEntity<String> sendMMS(@RequestParam("toPhoneNumber") String toPhoneNumber,
                                          @RequestParam("audioUrl") String audioUrl){
        log.info("Received request to send MMS to: {}", toPhoneNumber);
        twilioService.sendMMS(toPhoneNumber, audioUrl);
        return ResponseEntity.ok("MMS sent successfully");
    }
}
