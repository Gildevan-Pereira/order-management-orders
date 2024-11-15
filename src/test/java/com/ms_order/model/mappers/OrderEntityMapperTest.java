package com.ms_order.model.mappers;

import com.ms_order.fixture.OrderEntityFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderEntityMapperTest {

    @Test
    void shouldReturnDocument() {
       var orderEntity = OrderEntityFixture.buildDefault();
       var document = OrderEntityMapper.toDocument(orderEntity);

       assertThat(document.getOrderId()).isEqualTo(orderEntity.getId());
       assertThat(document.getCreatedAt()).isEqualTo(orderEntity.getCreatedAt());
       assertThat(document.getAmount()).isEqualTo(orderEntity.getAmount());
       assertThat(document.getStatus()).isEqualTo(orderEntity.getStatus());
       assertThat(document.getName()).isEqualTo(orderEntity.getName());
       assertThat(document.getCpf()).isEqualTo(orderEntity.getCpf());
       assertThat(document.getAddress()).isEqualTo(orderEntity.getAddress());
       assertThat(document.getPostalCode()).isEqualTo(orderEntity.getPostalCode());
       assertThat(document.getCity()).isEqualTo(orderEntity.getCity());
       assertThat(document.getState()).isEqualTo(orderEntity.getState());
       assertThat(document.getAttemptedPaymentAt()).isEqualTo(orderEntity.getAttemptedPaymentAt());
       assertThat(document.getItems().getFirst().getName())
               .isEqualTo(orderEntity.getItems().getFirst().getName());
       assertThat(document.getItems().getFirst().getDescription())
               .isEqualTo(orderEntity.getItems().getFirst().getDescription());
       assertThat(document.getItems().getFirst().getUnityPrice())
               .isEqualTo(orderEntity.getItems().getFirst().getUnityPrice());
       assertThat(document.getItems().getFirst().getCount())
               .isEqualTo(orderEntity.getItems().getFirst().getCount());
       assertThat(document.getItems().getLast().getName())
               .isEqualTo(orderEntity.getItems().getLast().getName());
       assertThat(document.getItems().getLast().getDescription())
               .isEqualTo(orderEntity.getItems().getLast().getDescription());
       assertThat(document.getItems().getLast().getUnityPrice())
               .isEqualTo(orderEntity.getItems().getLast().getUnityPrice());
       assertThat(document.getItems().getLast().getCount())
               .isEqualTo(orderEntity.getItems().getLast().getCount());

    }
}