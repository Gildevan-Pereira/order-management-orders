package com.ms_order.model.enums;

import com.ms_order.exception.BusinessException;

import java.util.List;

public enum OrderStatusEnum {
    CREATED,
    FINISHED;

    public static OrderStatusEnum fromName(String name) {
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()){
            if (statusEnum.name().equals(name)) {
                return statusEnum;
            }
        }
        throw new BusinessException("Invalid status given. Accepted values: " + List.of(OrderStatusEnum.values()));
    }
}
