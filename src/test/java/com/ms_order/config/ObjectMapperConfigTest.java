package com.ms_order.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectMapperConfigTest {

    @Test
    void objectMapper() {
        var mapperConfig = new ObjectMapperConfig();
        var mapper = mapperConfig.objectMapper();
        assertThat(mapper).isNotNull();
    }
}