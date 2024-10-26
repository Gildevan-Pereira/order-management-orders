package com.ms_order.model.dto.request;

import com.ms_order.model.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;

    public static OrderItemDto fromEntity(ItemEntity item) {
        return OrderItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .unityPrice(item.getUnityPrice())
                .count(item.getCount())
                .build();
    }
}
