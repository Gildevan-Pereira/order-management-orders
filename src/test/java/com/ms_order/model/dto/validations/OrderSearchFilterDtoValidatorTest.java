package com.ms_order.model.dto.validations;

import com.ms_order.fixture.OrderSearchFilterDtoFixture;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderSearchFilterDtoValidatorTest {

    private OrderSearchFilterDtoValidator validator;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new OrderSearchFilterDtoValidator();
    }

    @Test
    void shouldReturnTrueWhenAllFieldsIsNull() {
        var request = OrderSearchFilterDto.builder().build();

        Boolean response = validator.isValid(request, context);

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAllFieldsIsNotNullAndValid() {
        var request = OrderSearchFilterDtoFixture.buildDefault();

        Boolean response = validator.isValid(request, context);

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAllFieldsIsEmpty() {
        var request = OrderSearchFilterDtoFixture.buildEmpty();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStartDateIsNotNullAndEndDateIsNull() {
        var request = OrderSearchFilterDto.builder()
                .startDate(LocalDate.of(2023, 1, 1))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStartDateIsNullAndEndDateIsNotNull() {
        var request = OrderSearchFilterDto.builder()
                .endDate(LocalDate.of(2023, 1, 1))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStartDateIsAfterEndDate() {
        var request = OrderSearchFilterDto.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2023, 1, 1))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMinAmountIsNotNullAndMaxAmountIsNull() {
        var request = OrderSearchFilterDto.builder()
                .minAmount(BigDecimal.valueOf(100))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMinAmountIsNullAndMaxAmountIsNotNull() {
        var request = OrderSearchFilterDto.builder()
                .maxAmount(BigDecimal.valueOf(100))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMinAmountIsAfterMaxAmount() {
        var request = OrderSearchFilterDto.builder()
                .minAmount(BigDecimal.valueOf(150))
                .maxAmount(BigDecimal.valueOf(100))
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenNameHasSpaceBlank() {
        var request = OrderSearchFilterDto.builder()
                .name("  ")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenNameIsInvalid() {
        var request = OrderSearchFilterDto.builder()
                .name("1234")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCpfHasSpaceBlank() {
        var request = OrderSearchFilterDto.builder()
                .cpf("  ")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCpfIsInvalid() {
        var request = OrderSearchFilterDto.builder()
                .cpf("abcd")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCityHasSpaceBlank() {
        var request = OrderSearchFilterDto.builder()
                .city("  ")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCityIsInvalid() {
        var request = OrderSearchFilterDto.builder()
                .city("123")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStateHasSpaceBlank() {
        var request = OrderSearchFilterDto.builder()
                .state("  ")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStateIsInvalid() {
        var request = OrderSearchFilterDto.builder()
                .state("123")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }
}