package com.ms_order.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ms_order.exception.InternalException;
import com.ms_order.messages.MessageEnum;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;

public class JsonParserUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String toJson(Object obj) {

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
           throw new InternalException(MessageEnum.GENERIC_ERROR, HttpStatus.BAD_GATEWAY);
        }
    }

    public static <T> T fromJson(String input, Class<T> outputType) {
        try {
            return objectMapper.readValue(input, outputType);
        } catch (JsonProcessingException e) {
            throw new InternalException(MessageEnum.GENERIC_ERROR, HttpStatus.BAD_GATEWAY);
        }
    }

    public static <T> T fromBytes(byte[] input, Class<T> outputType) {
        try {
            String value = new String(input, StandardCharsets.UTF_8);
            return objectMapper.readValue(value, outputType);
        } catch (JsonProcessingException e) {
            throw new InternalException(MessageEnum.GENERIC_ERROR, HttpStatus.BAD_GATEWAY);
        }
    }
}
