package com.ms_order.model.dto.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = OrderSearchFilterDtoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderSearchFilterDtoValidation {
    String message()  default "Filters validation error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
