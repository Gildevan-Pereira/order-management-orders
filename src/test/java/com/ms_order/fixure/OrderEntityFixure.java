package com.ms_order.fixure;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.entity.OrderEntity;

public class OrderEntityFixure {

    public static OrderEntity buildDefault(CreateOrderRequestDto requestDto) {
       return OrderEntity.builder()
                .id(1)
                .name(requestDto.getName())
                .cpf(requestDto.getCpf())
                .address(requestDto.getAddress())
                .postalCode(requestDto.getPostalCode())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .build();
    }
}
