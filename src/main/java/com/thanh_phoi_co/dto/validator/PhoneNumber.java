package com.thanh_phoi_co.dto.validator;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER, ANNOTATION_TYPE})
@Constraint(validatedBy = PhoneValidator.class)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
