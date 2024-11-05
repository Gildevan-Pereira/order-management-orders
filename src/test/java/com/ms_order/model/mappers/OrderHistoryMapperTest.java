package com.ms_order.model.mappers;

import com.ms_order.fixture.OrderHistoryDocumentFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderHistoryMapperTest {

    @Test
    void ShouldReturnResponseDto() {
        var document = OrderHistoryDocumentFixture.buildDefault();
        var responseDocument = OrderHistoryMapper.toResponseDto(document);

        assertThat(responseDocument.getId()).isEqualTo(document.getId());
        assertThat(responseDocument.getOrderId()).isEqualTo(document.getOrderId());
        assertThat(responseDocument.getCreatedAt()).isEqualTo(document.getCreatedAt());
        assertThat(responseDocument.getHistoryCreatedAt()).isEqualTo(document.getHistoryCreatedAt());
        assertThat(responseDocument.getAmount()).isEqualTo(document.getAmount());
        assertThat(responseDocument.getStatus()).isEqualTo(document.getStatus());
        assertThat(responseDocument.getName()).isEqualTo(document.getName());
        assertThat(responseDocument.getCpf()).isEqualTo(document.getCpf());
        assertThat(responseDocument.getAddress()).isEqualTo(document.getAddress());
        assertThat(responseDocument.getPostalCode()).isEqualTo(document.getPostalCode());
        assertThat(responseDocument.getCity()).isEqualTo(document.getCity());
        assertThat(responseDocument.getState()).isEqualTo(document.getState());
        assertThat(responseDocument.getAttemptedPaymentAt()).isEqualTo(document.getAttemptedPaymentAt());
        assertThat(responseDocument.getItems().getFirst().getName())
                .isEqualTo(document.getItems().getFirst().getName());
        assertThat(responseDocument.getItems().getFirst().getDescription())
                .isEqualTo(document.getItems().getFirst().getDescription());
        assertThat(responseDocument.getItems().getFirst().getUnityPrice())
                .isEqualTo(document.getItems().getFirst().getUnityPrice());
        assertThat(responseDocument.getItems().getFirst().getCount())
                .isEqualTo(document.getItems().getFirst().getCount());
        assertThat(responseDocument.getItems().getLast().getName())
                .isEqualTo(document.getItems().getLast().getName());
        assertThat(responseDocument.getItems().getLast().getDescription())
                .isEqualTo(document.getItems().getLast().getDescription());
        assertThat(responseDocument.getItems().getLast().getUnityPrice())
                .isEqualTo(document.getItems().getLast().getUnityPrice());
        assertThat(responseDocument.getItems().getLast().getCount())
                .isEqualTo(document.getItems().getLast().getCount());

    }
}