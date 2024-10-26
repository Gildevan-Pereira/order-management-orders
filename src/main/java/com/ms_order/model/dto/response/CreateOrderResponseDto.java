package com.ms_order.model.dto.response;

import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponseDto {


    private Integer id;

    private LocalDateTime createdAt;

    private BigDecimal amount;

    private OrderStatusEnum status;

    private String name;

    private String cpf;

    private String address;

    private String postalCode;

    private String city;

    private String state;

    private List<OrderItemDto> items;

    public static CreateOrderResponseDto fromEntity(OrderEntity order) {
        return CreateOrderResponseDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .amount(order.getAmount())
                .status(order.getStatus())
                .name(order.getName())
                .cpf(order.getCpf())
                .address(order.getAddress())
                .postalCode(order.getPostalCode())
                .city(order.getCity())
                .state(order.getState())
                .items(order.getItems().stream()
                        .map(OrderItemDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
