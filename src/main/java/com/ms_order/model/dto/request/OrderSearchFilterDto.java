package com.ms_order.model.dto.request;

import com.ms_order.model.enums.OrderStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchFilterDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String status;
    private String name;
    private String cpf;
    private String city;
    private String state;
}
