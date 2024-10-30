package com.ms_order.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_order.exception.InternalException;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import org.springframework.http.HttpStatus;

public class JsonParserUtil {

    public static String toJson(Object obj) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
           throw new InternalException("An error has ocurred", "0002", HttpStatus.BAD_GATEWAY);
        }
    }
}
