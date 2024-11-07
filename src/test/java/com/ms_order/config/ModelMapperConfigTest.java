package com.ms_order.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    @Test
    void modelMapper() {
        var mapperConfig = new ModelMapperConfig();
        var mapper = mapperConfig.modelMapper();
        assertThat(mapper).isNotNull();
    }
}