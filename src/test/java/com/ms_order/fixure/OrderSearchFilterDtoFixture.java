package com.ms_order.fixure;

import com.ms_order.model.dto.request.OrderSearchFilterDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class OrderSearchFilterDtoFixture {

    public static OrderSearchFilterDto buildDefault() {
        return OrderSearchFilterDto.builder()
                .ids(Arrays.asList(1, 2, 3))
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .minAmount(new BigDecimal("50.00"))
                .maxAmount(new BigDecimal("500.00"))
                .status(Arrays.asList("CREATED", "PROCESSED", "REJECTED"))
                .name("John Doe")
                .cpf("12345678900")
                .city("New York")
                .state("NY")
                .build();
    }
}
