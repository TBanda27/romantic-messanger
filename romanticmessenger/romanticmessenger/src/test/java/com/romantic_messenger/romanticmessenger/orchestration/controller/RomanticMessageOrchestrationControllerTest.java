package com.romantic_messenger.romanticmessenger.orchestration.controller;

import com.romantic_messenger.romanticmessenger.orchestration.service.RomanticMessageOrchestrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// TODO: Add @WebMvcTest slice tests for both endpoints using MockMvc.
//       The orchestration service should be mocked so these stay fast and unit-level.
@WebMvcTest(RomanticMessageOrchestrationController.class)
class RomanticMessageOrchestrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RomanticMessageOrchestrationService orchestrationService;

    // TODO: POST /api/v1/messages/send with a valid body should return 202 ACCEPTED
    //       and a JSON body containing "jobId" and "statusEndpoint".
    @Test
    void sendRomanticMessage_validRequest_returns202() {
        // TODO: implement
    }

    // TODO: POST /api/v1/messages/send with a missing phoneNumber should return 400
    //       and a validation error message (Jakarta Bean Validation via @Valid).
    @Test
    void sendRomanticMessage_missingPhoneNumber_returns400() {
        // TODO: implement
    }

    // TODO: POST /api/v1/messages/send with a single-word bookTheme should return 400
    //       because ValidBookThemeValidator requires at least 2 words.
    @Test
    void sendRomanticMessage_singleWordTheme_returns400() {
        // TODO: implement
    }

    // TODO: GET /api/v1/messages/status/{jobId} for an existing job should return 200
    //       and expose currentStage + progressPercentage in the response body.
    @Test
    void getJobStatus_existingJob_returns200WithStage() {
        // TODO: implement
    }

    // TODO: GET /api/v1/messages/status/{jobId} for an unknown jobId should return 404.
    //       Relies on OrchestrationExceptionHandler mapping JobNotFoundException → 404.
    @Test
    void getJobStatus_unknownJobId_returns404() {
        // TODO: implement
    }
}
