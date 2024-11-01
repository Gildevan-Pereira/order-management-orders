package com.ms_order.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageEnum {

    GENERIC_ERROR("0001", "An error has occurred"),
    ORDER_HISTORY_NOT_FOUND("0101", "Order history not found for id: %s"),
    ORDER_NOT_FOUND("0100", "Order not found for id: %s"),
    INVALID_STATUS ("0200","Invalid status given. Accepted values: %s");

    private final String code;
    private final String message;


}
