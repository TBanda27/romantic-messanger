package com.romantic_messenger.romanticmessenger.claude.utils;

import com.anthropic.models.messages.MessageCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PromptGenerator {

    @Value("${claude.model.version}")
    private String claudeModelVersion;

    @Value("${claude.max-tokens}")
    private int claudeMaxTokens;

    private static final String PROMPT_TEMPLATE = """
            You are a romantic message generator. Create a heartfelt and touching romantic message based on the following theme:

            Theme: %s

            The message should be sincere, emotional, and tailored to express deep feelings of love and affection. Ensure the message is engaging and resonates with the reader.
            Try to make the message a little naughty and playful, while still being romantic.

            Constraints:
            - Use a warm and loving tone.
            - Avoid clichés and generic phrases.
            - Make it unique and personal.
            - Message must be in English.
            - Message must be between 100 and 200 words long.
            - Give the message as a single paragraph, do not use line breaks.

            Respond only with the romantic message, without any additional commentary or explanation.
            """;

    public MessageCreateParams buildMessageRequest(String bookTheme){
        log.debug("Building message request for theme: {}", bookTheme);
        String prompt = String.format(PROMPT_TEMPLATE, bookTheme);
        return MessageCreateParams.builder()
                .model(claudeModelVersion)
                .maxTokens(claudeMaxTokens)
                .addUserMessage(prompt)
                .build();
    }


}
