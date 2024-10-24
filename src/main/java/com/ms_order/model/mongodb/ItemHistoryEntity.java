package com.ms_order.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemHistoryEntity {

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;
}