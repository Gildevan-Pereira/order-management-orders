package com.ms_order.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdatedDto {

    private Integer orderId;
    private String status;
    private LocalDateTime attemptedPaymentAt;
}
