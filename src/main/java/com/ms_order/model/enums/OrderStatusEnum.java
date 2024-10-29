package com.ms_order.model.enums;

import com.ms_order.exception.BusinessException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        throw new BusinessException("Invalid status given. Accepted values: " + List.of(OrderStatusEnum.values()));
    }

    public static boolean isValid(String value) {
        return Arrays.stream(OrderStatusEnum.values())
                .anyMatch(status -> status.name().equalsIgnoreCase(value));
    }

    public static String getAcceptedValues() {
        return Arrays.stream(OrderStatusEnum.values())
                .map(OrderStatusEnum::name)
                .collect(Collectors.joining(", "));
    }


}
