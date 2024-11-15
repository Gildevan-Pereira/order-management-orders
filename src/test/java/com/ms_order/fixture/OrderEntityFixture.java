package com.ms_order.fixture;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderEntityFixture {

    public static OrderEntity buildFromRequestDto(CreateOrderRequestDto requestDto) {
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

    public static OrderEntity buildDefault() {

        var item1 = ItemEntityFixture.buildDefault(BigDecimal.valueOf(100), 2);
        var item2 = ItemEntityFixture.buildDefault(BigDecimal.valueOf(50), 1);

        return OrderEntity.builder()
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


}
