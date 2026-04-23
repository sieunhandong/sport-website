package com.thanh_phoi_co.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, LocalDateTime> {
    private String pattern;
    private boolean future;
    private boolean past;

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.future = constraintAnnotation.future();
        this.past = constraintAnnotation.past();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (future && value.isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date must be in the future")
                    .addConstraintViolation();
            return false;
        }

        if (past && value.isAfter(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date must be in the past")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
