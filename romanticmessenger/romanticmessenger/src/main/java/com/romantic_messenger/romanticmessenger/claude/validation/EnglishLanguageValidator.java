package com.romantic_messenger.romanticmessenger.claude.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EnglishLanguageValidator implements ConstraintValidator<EnglishLanguage, String> {

    private static final Pattern ENGLISH_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,;:!?'\"()-]+$");

    private static final Pattern NON_ENGLISH_SCRIPTS = Pattern.compile(
            "[\\p{InArabic}\\p{InCyrillic}\\p{InHebrew}\\p{InDevanagari}" +
            "\\p{InCJKUnifiedIdeographs}\\p{InHiragana}\\p{InKatakana}" +
            "\\p{InHangulSyllables}\\p{InThai}\\p{InGreek}]"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        if (NON_ENGLISH_SCRIPTS.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Book theme contains non-English characters. Please provide the theme in English."
            ).addConstraintViolation();
            return false;
        }

        if (!ENGLISH_PATTERN.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Book theme contains invalid characters. Only English letters, numbers, and common punctuation are allowed."
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
