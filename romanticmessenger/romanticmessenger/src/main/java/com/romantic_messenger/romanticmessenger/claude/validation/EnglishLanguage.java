package com.romantic_messenger.romanticmessenger.claude.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnglishLanguageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnglishLanguage {
    String message() default "Book theme must be in English";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
