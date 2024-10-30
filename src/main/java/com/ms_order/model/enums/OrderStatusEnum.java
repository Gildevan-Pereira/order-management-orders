package com.ms_order.model.enums;

import com.ms_order.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.List;

public enum OrderStatusEnum {
    CREATED,
    PROCESSED,
    REJECTED;

    public static OrderStatusEnum fromName(String name) {
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()){
            if (statusEnum.name().equals(name)) {
                return statusEnum;
            }
        }
        throw new BusinessException(
                "Invalid status given. Accepted values: " + List.of(OrderStatusEnum.values()),
                "1000",
                HttpStatus.BAD_REQUEST);
    }

}
