package com.ms_order.fixture;

import com.ms_order.model.dto.request.OrderItemDto;

import java.math.BigDecimal;

public class OrderItemDtoFixture {

    public static OrderItemDto buildDefault(Integer count, BigDecimal unityPrice) {
       return OrderItemDto.builder()
                .name("Item 1")
                .description("Descrição")
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }
    public static OrderItemDto buildFromParams(String name, String description, BigDecimal unityPrice, Integer count) {
        return OrderItemDto.builder()
                .name(name)
                .description(description)
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }
}
