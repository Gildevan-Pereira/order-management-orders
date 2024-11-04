package com.ms_order.fixture;

import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

public class OrderHistoryDocumentFixture {

    public static OrderHistoryDocument buildDefault() {

        var item1 = ItemHistoryDocumentFixture.buildDefault(BigDecimal.valueOf(100), 2);
        var item2 = ItemHistoryDocumentFixture.buildDefault(BigDecimal.valueOf(50), 1);

        return OrderHistoryDocument.builder()
                .orderId(1)
                .amount(BigDecimal.valueOf(250.00))
                .status(OrderStatusEnum.CREATED)
                .name("Jon Doe I")
                .cpf("12345678901")
                .address("Rua X")
                .postalCode("12345678")
                .city("Cidade X")
                .state("SP")
                .items(List.of(item1, item2))
                .build();
    }

    public static OrderHistoryDocument buildFromResponseDto(OrderResponseDto dto) {

        ModelMapper modelMapper = new ModelMapper();

        var list = dto.getItems().stream()
                .map(itemDto -> modelMapper.map(itemDto, ItemHistoryDocument.class))
                .toList();

        return OrderHistoryDocument.builder()
               .orderId(dto.getId())
               .amount(dto.getAmount())
               .status(dto.getStatus())
               .name(dto.getName())
               .cpf(dto.getCpf())
               .address(dto.getAddress())
               .postalCode(dto.getPostalCode())
               .city(dto.getCity())
               .state(dto.getState())
               .items(list)
               .build();
    }

}
