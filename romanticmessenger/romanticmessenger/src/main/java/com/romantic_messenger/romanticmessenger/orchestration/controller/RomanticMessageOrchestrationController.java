package com.romantic_messenger.romanticmessenger.orchestration.controller;

import com.romantic_messenger.romanticmessenger.orchestration.dto.JobStatusResponse;
import com.romantic_messenger.romanticmessenger.orchestration.dto.RomanticMessageRequest;
import com.romantic_messenger.romanticmessenger.orchestration.dto.RomanticMessageResponse;
import com.romantic_messenger.romanticmessenger.orchestration.service.RomanticMessageOrchestrationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@Slf4j
public class RomanticMessageOrchestrationController {

    private final RomanticMessageOrchestrationService orchestrationService;

    public RomanticMessageOrchestrationController(
            RomanticMessageOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/send")
    public ResponseEntity<RomanticMessageResponse> sendRomanticMessage(
            @Valid @RequestBody RomanticMessageRequest request) {
        log.info("Received request to send romantic message to: {}, theme: {}",
            request.phoneNumber(), request.bookTheme());

        String jobId = orchestrationService.initiateMessagePipeline(request);
        RomanticMessageResponse response = RomanticMessageResponse.created(jobId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<JobStatusResponse> getJobStatus(@PathVariable String jobId) {
        log.debug("Status check for job: {}", jobId);

        JobStatusResponse status = orchestrationService.getJobStatus(jobId);

        return ResponseEntity.ok(status);
    }
}
