package com.ms_order.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageEnum {

    GENERIC_ERROR("0001", "An error has occurred"),
    ORDER_HISTORY_NOT_FOUND("0101", "Order history not found for id: %s"),
    ORDER_NOT_FOUND("0100", "Order not found for id: %s"),
    INVALID_STATUS ("0200","Invalid status given. Accepted values: %s"),

//    DTO validations
    RANGE_DATE_ERROR("0300", "The startDate and endDate fields must be sent together"),
    RANGE_DATE_INVALID("0301", "The startDate field must be less than endDate"),
    RANGE_AMOUNT_ERROR("0302", "The minAmount and maxAmount fields must be sent together"),
    RANGE_AMOUNT_INVALID("0303", "The minAmount field must be less than maxAmount"),
    CLIENT_NAME_INVALID("0304", "Client name is invalid"),
    CPF_INVALID("0305", "Client cpf is invalid"),
    CITY_INVALID("0306", "Client city is invalid"),
    STATE_INVALID("0307", "Client state is invalid");

    private final String code;
    private final String message;

    public String joinCodeAndMessage() {
        return this.code + "#" + this.message;
    }


}
