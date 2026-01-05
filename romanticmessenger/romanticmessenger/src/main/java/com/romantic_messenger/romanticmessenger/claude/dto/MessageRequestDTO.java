package com.romantic_messenger.romanticmessenger.claude.dto;

import com.romantic_messenger.romanticmessenger.claude.validation.EnglishLanguage;
import com.romantic_messenger.romanticmessenger.claude.validation.ValidBookTheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequestDTO(
        @NotBlank(message = "Book theme is required")
        @Size(min = 10, max = 200, message = "Book theme must be between 10 and 200 characters long")
        @EnglishLanguage
        @ValidBookTheme
        String bookTheme
) {
}
