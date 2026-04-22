package com.thanh_phoi_co.dto.validator;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Constraint(validatedBy = LocalDateTimeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocalDateTime {
    String message() default "Invalid date-time format";
    String pattern() default "yyyy-MM-dd HH:mm:ss";
    boolean future() default false;
    boolean past() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
