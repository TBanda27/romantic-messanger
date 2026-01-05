package com.romantic_messenger.romanticmessenger.claude.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.TextBlock;
import com.romantic_messenger.romanticmessenger.claude.utils.PromptGenerator;
import com.romantic_messenger.romanticmessenger.claude.dto.MessageResponseDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RomanticMessageService {
    private final PromptGenerator promptGenerator;
    private final AnthropicClient anthropicClient;

    public RomanticMessageService(PromptGenerator promptGenerator, AnthropicClient anthropicClient) {
        this.promptGenerator = promptGenerator;
        this.anthropicClient = anthropicClient;
    }

    public MessageResponseDTO createRomanticMessage(@Valid com.romantic_messenger.romanticmessenger.dto.MessageRequestDTO messageRequestDTO) {
        log.info("Creating RomanticMessage request for message: {}", messageRequestDTO);
        MessageCreateParams messageCreateParams = promptGenerator.buildMessageRequest(messageRequestDTO.bookTheme());
        Message response = anthropicClient.messages().create(messageCreateParams);
        String message = extractMessageFromResponse(response);
        return new MessageResponseDTO(message);
    }

    private String extractMessageFromResponse(Message response) {
        return response.content().stream()
                .flatMap(contentBlock -> contentBlock.text().stream())
                .map(TextBlock::text)
                .findFirst()
                .orElse("");
    }
}
