package com.ms_order.rabbitmq;

import com.ms_order.exception.InternalException;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.event.OrderCreatedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderPublisherTest {

    private CreateOrderPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;

    private final String exchange = "created_order_exchange";
    private final String routingKey = "created_order_routing_key";

    @BeforeEach
    void setUp() {
        publisher = new CreateOrderPublisher(rabbitTemplate, exchange, routingKey);
    }

    @Test
    void sendSuccessful() {
        var orderCreatedDto = OrderCreatedDto.builder().orderId(1).amount(BigDecimal.valueOf(100.00)).build();
        publisher.send(orderCreatedDto);

        String json = "{\"orderId\":1,\"amount\":100.0}";

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, json);
    }

    @Test
    void sendShouldThrowsInternalExceptionWhenException() {
        var orderCreatedDto = OrderCreatedDto.builder().orderId(1).amount(BigDecimal.valueOf(100.00)).build();

        doThrow(RuntimeException.class).when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        Executable executable = () -> publisher.send(orderCreatedDto);

        InternalException exception = assertThrows(InternalException.class, executable);
        assertThat(exception.getCode()).isEqualTo(MessageEnum.ERROR_PUBLISHER.getCode());
        assertThat(exception.getMessage()).isEqualTo(MessageEnum.ERROR_PUBLISHER.getMessage());
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_GATEWAY);
    }

}