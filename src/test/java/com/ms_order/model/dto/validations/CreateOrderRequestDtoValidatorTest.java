package com.ms_order.model.dto.validations;

import com.ms_order.fixture.OrderItemDtoFixture;
import com.ms_order.fixture.OrderRequestDtoFixture;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderRequestDtoValidatorTest {

    private CreateOrderRequestDtoValidator validator;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new CreateOrderRequestDtoValidator();
    }

    @Test
    void shouldReturnTrueWhenAllFieldsIsValid() {
        var item1 = OrderItemDtoFixture.buildDefault(2, BigDecimal.valueOf(100));
        var item2 = OrderItemDtoFixture.buildDefault(1, BigDecimal.valueOf(50));
        var request = OrderRequestDtoFixture.buildDefault(List.of(item1, item2));

        Boolean response = validator.isValid(request, context);

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAllFieldsIsInvalid() {

        var item = OrderItemDto.builder()
                .name("12")
                .description("x")
                .unityPrice(null)
                .count(-1)
                .build();

        var request = CreateOrderRequestDto.builder()
                .name("123")
                .cpf("abc")
                .address("x")
                .postalCode("x")
                .city("x")
                .state("123")
                .items(List.of(item))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsNull() {
        var request = CreateOrderRequestDto.builder().build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsEmpty() {
        var request = CreateOrderRequestDto.builder()
                .name("")
                .cpf("")
                .address("")
                .postalCode("")
                .city("")
                .state("")
                .items(List.of())
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

}