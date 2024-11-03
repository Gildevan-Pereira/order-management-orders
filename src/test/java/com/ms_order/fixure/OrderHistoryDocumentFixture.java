package com.ms_order.fixure;

import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;
import org.modelmapper.ModelMapper;

public class OrderHistoryDocumentFixture {

    public static OrderHistoryDocument buildDefault(OrderResponseDto dto) {

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
