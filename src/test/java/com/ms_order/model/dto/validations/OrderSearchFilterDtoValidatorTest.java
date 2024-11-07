package com.ms_order.model.dto.validations;

import com.ms_order.fixture.OrderSearchFilterDtoFixture;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void shouldReturnFalseWhenNameHasSpaceBlank() {
        var request = OrderSearchFilterDto.builder()
                .name("  ")
                .build();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        Boolean response = validator.isValid(request, context);

        assertThat(response).isFalse();
    }

}