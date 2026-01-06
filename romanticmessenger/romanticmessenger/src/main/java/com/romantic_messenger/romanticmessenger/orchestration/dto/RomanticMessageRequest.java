package com.romantic_messenger.romanticmessenger.orchestration.dto;

import com.romantic_messenger.romanticmessenger.claude.validation.EnglishLanguage;
import com.romantic_messenger.romanticmessenger.claude.validation.ValidBookTheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RomanticMessageRequest(
    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be in E.164 format (e.g., +1234567890)"
    )
    String phoneNumber,

    @NotBlank(message = "Book theme is required")
    @Size(min = 10, max = 200, message = "Book theme must be between 10 and 200 characters")
    @EnglishLanguage
    @ValidBookTheme
    String bookTheme
) {}
