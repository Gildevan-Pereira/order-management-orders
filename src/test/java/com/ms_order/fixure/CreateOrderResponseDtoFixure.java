package com.ms_order.fixure;

import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.entity.OrderEntity;

import java.util.List;

public class CreateOrderResponseDtoFixure {

    public static OrderResponseDto buildDefault(OrderEntity orderEntity, List<OrderItemDto> items) {
        return OrderResponseDto.builder()
                .id(orderEntity.getId())
                .status(orderEntity.getStatus())
                .name(orderEntity.getName())
                .cpf(orderEntity.getCpf())
                .address(orderEntity.getAddress())
                .postalCode(orderEntity.getPostalCode())
                .city(orderEntity.getCity())
                .state(orderEntity.getState())
                .items(items)
                .build();
    }
}
