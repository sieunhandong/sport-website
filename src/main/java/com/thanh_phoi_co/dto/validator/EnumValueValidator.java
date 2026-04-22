package com.thanh_phoi_co.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Enum<?>> {
    private Enum<?>[] acceptedValues;

    @Override
    public void initialize(EnumValue annotation) {
        // Lấy tất cả các giá trị của enum được truyền vào
        acceptedValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // Nếu giá trị là null, coi là hợp lệ (nếu muốn thì có thể xử lý khác)
        }

        // Kiểm tra xem giá trị của enum có nằm trong danh sách các giá trị hợp lệ không
        return Arrays.stream(acceptedValues)
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(value.name()));
    }
}
