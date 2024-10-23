package com.ms_order.fixure;

import com.ms_order.model.dto.request.OrderItemDto;

import java.math.BigDecimal;

public class OrdemItemDtoFixure {

    public static OrderItemDto buildDefault(Integer count, BigDecimal unityPrice) {
       return OrderItemDto.builder()
                .name("Item")
                .description("Descrição")
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }
}
