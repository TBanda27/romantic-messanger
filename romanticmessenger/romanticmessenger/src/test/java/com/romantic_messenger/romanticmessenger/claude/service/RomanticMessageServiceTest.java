package com.romantic_messenger.romanticmessenger.claude.service;

import com.anthropic.client.AnthropicClient;
import com.romantic_messenger.romanticmessenger.claude.dto.MessageRequestDTO;
import com.romantic_messenger.romanticmessenger.claude.utils.PromptGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Unit-test RomanticMessageService with a mocked AnthropicClient.
//       Do not call the real Claude API — these tests must be offline and deterministic.
@ExtendWith(MockitoExtension.class)
class RomanticMessageServiceTest {

    @Mock
    private AnthropicClient anthropicClient;

    @Mock
    private PromptGenerator promptGenerator;

    @InjectMocks
    private RomanticMessageService romanticMessageService;

    // TODO: When the Claude API returns a TextBlock, createRomanticMessage should
    //       extract the text and return it wrapped in a MessageResponseDTO.
    @Test
    void createRomanticMessage_validResponse_returnsExtractedText() {
        // TODO: stub anthropicClient.messages().create(...) to return a fake Message
        //       with one TextBlock, then assert romanticMessage() equals that text.
    }

    // TODO: When the Claude API response contains no TextBlock (empty content list),
    //       createRomanticMessage should return a MessageResponseDTO with an empty string
    //       rather than throwing a NullPointerException.
    @Test
    void createRomanticMessage_emptyContentBlock_returnsEmptyString() {
        // TODO: implement
    }

    // TODO: When anthropicClient.messages().create(...) throws (e.g. network error or
    //       rate limit), the exception should propagate so GlobalExceptionHandler can map it.
    @Test
    void createRomanticMessage_apiThrows_propagatesException() {
        // TODO: implement
    }
}
