package com.ms_order.model.dto.request;

import com.ms_order.model.dto.validations.OrderItemDtoValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@OrderItemDtoValidation
public class OrderItemDto {

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;

}
