package com.romantic_messenger.romanticmessenger.claude.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidBookThemeValidator implements ConstraintValidator<ValidBookTheme, String> {

    private static final Pattern REPEATED_CHARS = Pattern.compile("(.)\\1{4,}");
    private static final Pattern ONLY_SPECIAL_CHARS = Pattern.compile("^[^a-zA-Z0-9]+$");

    private static final List<String> INAPPROPRIATE_KEYWORDS = Arrays.asList(
            "xxx", "porn", "sex", "explicit", "nsfw"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String normalized = value.toLowerCase().trim();

        if (REPEATED_CHARS.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Book theme appears to be gibberish. Please provide a valid book theme."
            ).addConstraintViolation();
            return false;
        }

        if (ONLY_SPECIAL_CHARS.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Book theme must contain letters or numbers, not only special characters."
            ).addConstraintViolation();
            return false;
        }

        for (String keyword : INAPPROPRIATE_KEYWORDS) {
            if (normalized.contains(keyword)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Book theme contains inappropriate content. Please provide a different theme."
                ).addConstraintViolation();
                return false;
            }
        }

        long wordCount = Arrays.stream(normalized.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .count();

        if (wordCount < 2) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Book theme should contain at least 2 words to be meaningful."
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
