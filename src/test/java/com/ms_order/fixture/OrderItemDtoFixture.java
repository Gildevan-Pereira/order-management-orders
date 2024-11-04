package com.ms_order.fixture;

import com.ms_order.model.dto.request.OrderItemDto;

import java.math.BigDecimal;

public class OrderItemDtoFixture {

    public static OrderItemDto buildDefault(Integer count, BigDecimal unityPrice) {
       return OrderItemDto.builder()
                .name("Item")
                .description("Descrição")
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }
}
