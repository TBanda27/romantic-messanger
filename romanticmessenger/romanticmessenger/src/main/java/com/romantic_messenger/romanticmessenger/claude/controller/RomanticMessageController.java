package com.romantic_messenger.romanticmessenger.claude.controller;

import com.romantic_messenger.romanticmessenger.claude.dto.MessageResponseDTO;
import com.romantic_messenger.romanticmessenger.claude.service.RomanticMessageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/romantic-messages")
@Slf4j
public class RomanticMessageController {

    private final RomanticMessageService romanticMessageService;

    public RomanticMessageController(RomanticMessageService romanticMessageService) {
        this.romanticMessageService = romanticMessageService;
    }

    @PostMapping("")
    public ResponseEntity<MessageResponseDTO> generateRomanticMessage(@Valid @RequestBody com.romantic_messenger.romanticmessenger.dto.MessageRequestDTO messageRequestDTO) {
        log.info("generateRomanticMessage: Received request: {}", messageRequestDTO);
        return ResponseEntity.ok(romanticMessageService.createRomanticMessage(messageRequestDTO));
    }
}
