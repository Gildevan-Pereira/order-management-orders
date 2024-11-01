package com.ms_order.model.enums;

import com.ms_order.exception.BusinessException;
import com.ms_order.messages.MessageEnum;
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
        throw new BusinessException(MessageEnum.INVALID_STATUS, List.of(OrderStatusEnum.values()).toString(), HttpStatus.BAD_REQUEST);
    }

}
