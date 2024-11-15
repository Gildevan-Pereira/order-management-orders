package com.ms_order.model.dto.response;

import com.ms_order.model.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponseDto {

    private String id;

    private Integer orderId;

    private LocalDateTime createdAt;

    private LocalDateTime historyCreatedAt;

    private BigDecimal amount;

    private OrderStatusEnum status;

    private String name;

    private String cpf;

    private String address;

    private String postalCode;

    private String city;

    private String state;

    private LocalDateTime attemptedPaymentAt;

    private List<OrderHistoryItemDto> items;

}
