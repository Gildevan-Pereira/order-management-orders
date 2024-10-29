package com.ms_order.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_order.exception.BusinessException;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;

public class ConvertJson {


    public static String convertObjectToString(OrderCreatedDto dto) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new BusinessException(e.getMessage(), e.getCause());
        }
    }
}
