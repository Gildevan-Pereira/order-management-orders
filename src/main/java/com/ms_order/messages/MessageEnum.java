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
    CLIENT_NAME_INVALID("0304", "Client name is null or invalid"),
    CPF_INVALID("0305", "Client cpf is null or invalid"),
    CITY_INVALID("0306", "Client city is null or invalid"),
    STATE_INVALID("0307", "Client state is null or invalid"),
    ADDRESS_INVALID("0308", "Client address is null or invalid" ),
    POSTAL_CODE_INVALID("0309", "Client postal code is null or invalid"),
    ITEMS_NOT_EMPTY("0310", "List items is null or empty"),
    ITEM_NAME_INVALID("0311", "Item name is null or invalid"),
    ITEM_DESCRIPTION_INVALID("0312", "Item description is null or invalid"),
    ITEM_UNITY_PRICE_INVALID("0313", "Item unity price is null or invalid"),
    ITEM_COUNT_INVALID("0314", "Item count is null or invalid");

    private final String code;
    private final String message;

    public String joinCodeAndMessage() {
        return this.code + "#" + this.message;
    }

}
