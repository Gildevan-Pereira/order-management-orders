package com.ms_order.fixture;

import com.ms_order.model.dto.event.OrderUpdatedDto;

import java.time.LocalDateTime;

public class OrderUpdatedDtoFixture {

    public static OrderUpdatedDto buildDefault() {
        return OrderUpdatedDto.builder()
                .orderId(1)
                .status("PROCESSED")
                .attemptedPaymentAt(LocalDateTime.of(2024,11, 3, 20, 10))
                .build();
    }
}
