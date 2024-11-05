package com.ms_order.model.mappers;

import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;

public class OrderEntityMapper {

    public static OrderHistoryDocument toDocument(OrderEntity orderEntity) {

        var itemsDocument = orderEntity.getItems().stream().map(itemEntity -> ItemHistoryDocument.builder()
                    .name(itemEntity.getName())
                    .description(itemEntity.getDescription())
                    .unityPrice(itemEntity.getUnityPrice())
                    .count(itemEntity.getCount())
                    .build())
                .toList();

        return OrderHistoryDocument.builder()
                .orderId(orderEntity.getId())
                .createdAt(orderEntity.getCreatedAt())
                .amount(orderEntity.getAmount())
                .status(orderEntity.getStatus())
                .name(orderEntity.getName())
                .cpf(orderEntity.getCpf())
                .address(orderEntity.getAddress())
                .postalCode(orderEntity.getPostalCode())
                .city(orderEntity.getCity())
                .state(orderEntity.getState())
                .attemptedPaymentAt(orderEntity.getAttemptedPaymentAt())
                .items(itemsDocument)
                .build();
    }
}
