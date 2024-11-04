package com.ms_order.model.mappers;

import com.ms_order.model.dto.response.OrderHistoryItemDto;
import com.ms_order.model.dto.response.OrderHistoryResponseDto;
import com.ms_order.model.mongodb.OrderHistoryDocument;

public class OrderHistoryMapper {

    public static OrderHistoryResponseDto toResponseDto(OrderHistoryDocument document) {

        var itemsDto = document.getItems().stream().map(item -> OrderHistoryItemDto.builder()
                        .name(item.getName())
                        .description(item.getDescription())
                        .unityPrice(item.getUnityPrice())
                        .count(item.getCount())
                        .build())
                .toList();

        return OrderHistoryResponseDto.builder()
                .id(document.getId())
                .orderId(document.getOrderId())
                .status(document.getStatus())
                .name(document.getName())
                .cpf(document.getCpf())
                .address(document.getAddress())
                .postalCode(document.getPostalCode())
                .city(document.getCity())
                .state(document.getState())
                .items(itemsDto)
                .build();
    }
}
