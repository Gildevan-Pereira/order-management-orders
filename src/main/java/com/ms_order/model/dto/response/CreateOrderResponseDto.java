package com.ms_order.model.dto.response;

import com.ms_order.model.dto.request.OrderItemDto;
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

    private LocalDateTime attemptedPaymentAt;

    private List<OrderItemDto> items;

}
