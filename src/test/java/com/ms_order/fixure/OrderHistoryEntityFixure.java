package com.ms_order.fixure;

import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.mongodb.ItemHistoryEntity;
import com.ms_order.model.mongodb.OrderHistoryEntity;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderHistoryEntityFixure {

    public static OrderHistoryEntity buildeDefault(CreateOrderResponseDto dto) {

        ModelMapper modelMapper = new ModelMapper();

        var list = dto.getItems().stream()
                .map(itemDto -> modelMapper.map(itemDto, ItemHistoryEntity.class))
                .toList();

       return OrderHistoryEntity.builder()
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
