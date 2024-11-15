package com.ms_order.util;

import com.ms_order.exception.InternalException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonParserUtilTest {

    static class TestDto {
        public String name;
    }

    @Test
    void toJsonSuccessful() {
        TestDto testDto = new TestDto();
        testDto.name = "John Doe";

        var json = JsonParserUtil.toJson(testDto);
        assertThat(json).isEqualTo("{\"name\":\"John Doe\"}");
    }

    @Test
    void toJsonThrowsInternalExceptionWhenObjectIsNull() {
        assertThrows(InternalException.class, () -> JsonParserUtil.toJson(null));
    }

    @Test
    void toJsonThrowsInternalExceptionWhenJsonProcessingException() {
        assertThrows(InternalException.class, () -> JsonParserUtil.toJson(System.in));
    }

    @Test
    void fromBytesSuccessful() {
        var testDto = JsonParserUtil.fromBytes("{\"name\":\"John Doe\"}".getBytes(), TestDto.class);
        assertThat(testDto.name).isEqualTo("John Doe");
    }

    @Test
    void fromBytesThrowsInternalExceptionWhenInputIsNull() {
        assertThrows(InternalException.class, () -> JsonParserUtil.fromBytes(null, TestDto.class));
    }

    @Test
    void fromBytesThrowsInternalExceptionWhenJsonProcessingException() {
        assertThrows(InternalException.class, () -> JsonParserUtil.fromBytes("name:John Doe".getBytes(), TestDto.class));
    }

}