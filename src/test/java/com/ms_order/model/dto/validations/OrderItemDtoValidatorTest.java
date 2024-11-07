package com.ms_order.model.dto.validations;

import com.ms_order.fixture.OrderItemDtoFixture;
import com.ms_order.model.dto.request.OrderItemDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemDtoValidatorTest {

    private OrderItemDtoValidator validator;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new OrderItemDtoValidator();
    }

    @Test
    void shouldReturnTrueWhenAllFieldsIsValid() {
        var request = OrderItemDtoFixture.buildDefault(2, BigDecimal.valueOf(100));

        Boolean response = validator.isValid(request, context);

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsNull() {
        var request = OrderItemDto.builder().build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsEmpty() {
        var request = OrderItemDto.builder()
                .name("")
                .description("")
                .unityPrice(null)
                .count(null)
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsInvalidAndUnityPriceHasScaleInvalid() {
        var request = OrderItemDto.builder()
                .name("123")
                .description("x")
                .unityPrice(BigDecimal.valueOf(100.883))
                .count(-1)
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsInvalidAndUnityPriceIsNegative() {
        var request = OrderItemDto.builder()
                .name("123")
                .description("x")
                .unityPrice(BigDecimal.valueOf(-100.883))
                .count(-1)
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsInvalidAndUnityPriceIsZero() {
        var request = OrderItemDto.builder()
                .name("123")
                .description("x")
                .unityPrice(BigDecimal.valueOf(0))
                .count(-1)
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }
}