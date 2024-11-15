package com.ms_order.model.dto.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateOrderRequestDtoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateOrderRequestDtoValidation {
    String message()  default "Field validation error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
