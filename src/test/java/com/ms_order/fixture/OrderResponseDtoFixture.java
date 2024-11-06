package com.ms_order.fixture;

import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDtoFixture {

    public static OrderResponseDto buildDefault() {

        var item1 = OrderItemDtoFixture.buildDefault(2, BigDecimal.valueOf(100));
        var item2 = OrderItemDtoFixture.buildDefault(1, BigDecimal.valueOf(50));

        return OrderResponseDto.builder()
                .id(1)
                .amount(BigDecimal.valueOf(250.0))
                .status(OrderStatusEnum.CREATED)
                .name("Jon Doe I")
                .cpf("12345678901")
                .address("Rua X")
                .postalCode("12345678")
                .city("Cidade X")
                .state("SP")
                .attemptedPaymentAt(LocalDateTime.parse("2024-11-01T19:35:41.00"))
                .items(List.of(item1, item2))
                .build();
    }

    public static OrderResponseDto buildFromEntity(OrderEntity orderEntity, List<OrderItemDto> items) {
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
