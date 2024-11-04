package com.ms_order.model.enums;

import com.ms_order.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusEnumTest {

    @Test
    void fromNameShouldThrowsBusinessExceptionWhenNameIsInvalid() {
        assertThrows(BusinessException.class, () -> OrderStatusEnum.fromName("INVALID"));
    }

    @Test
    void fromNameShouldSuccessful() {
        assertThat(OrderStatusEnum.fromName("CREATED")).isEqualTo(OrderStatusEnum.CREATED);
    }
}