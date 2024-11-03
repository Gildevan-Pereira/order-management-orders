package com.ms_order.model.dto.validations;

import com.ms_order.model.dto.request.OrderItemDto;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderItemDtoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderItemDtoValidation {
    String message()  default "Fields validation error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
