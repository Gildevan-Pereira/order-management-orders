package com.ms_order.fixture;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;

import java.util.List;

public class CreateOrderRequestDtoFixture {

    public static CreateOrderRequestDto buildDefault(List<OrderItemDto> list){
        return CreateOrderRequestDto.builder()
                .name("Jon Doe")
                .cpf("12345678901")
                .address("Rua x")
                .postalCode("12345678")
                .city("Cidade x")
                .state("SP")
                .items(list)
                .build();
    }
}
