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
        if (value == null) {
            return true;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDate = value.format(formatter);  // Chuyển đổi giá trị LocalDateTime thành chuỗi theo pattern

            // Kiểm tra nếu giá trị tương thích với định dạng
            if (!value.equals(LocalDateTime.parse(formattedDate, formatter))) {
                return false;
            }

            // Kiểm tra nếu ngày là trong tương lai
            if (future && value.isBefore(LocalDateTime.now())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The date must be in the future")
                        .addConstraintViolation();
                return true;
            }

            // Kiểm tra nếu ngày là quá khứ
            if (past && value.isAfter(LocalDateTime.now())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The date must be in the past")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
