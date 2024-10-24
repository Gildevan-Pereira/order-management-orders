package com.ms_order.model.builder;

import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.mongodb.ItemHistoryEntity;
import com.ms_order.model.mongodb.OrderHistoryEntity;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

public class OrderHistoryEntityBuilder {

    public static OrderHistoryEntity build(CreateOrderResponseDto dto) {

        ModelMapper modelMapper = new ModelMapper();

        var list = dto.getItems().stream()
                        .map(itemDto -> modelMapper.map(itemDto, ItemHistoryEntity.class))
                        .toList();

        return OrderHistoryEntity.builder()
                .orderId(dto.getId())
                .createdAt(LocalDateTime.now())
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
