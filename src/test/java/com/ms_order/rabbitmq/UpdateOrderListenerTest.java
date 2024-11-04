package com.ms_order.rabbitmq;

import com.ms_order.exception.BusinessException;
import com.ms_order.exception.InternalException;
import com.ms_order.rabbitmq.dto.OrderUpdatedDto;
import com.ms_order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderListenerTest {

    private UpdateOrderListener listener;
    @Mock
    private OrderService orderService;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Captor
    private ArgumentCaptor<OrderUpdatedDto> argumentCaptor;

    private final String exchange = "exchange";
    private final String retry = "retry";
    private final String dead = "dead";


    @BeforeEach
    void setUp() {
        Integer maxRetry = 3;
        listener = new UpdateOrderListener(orderService, rabbitTemplate, exchange, retry, dead, maxRetry);
    }

    @Test
    void shouldUpdateOrderSuccessful() {
        String json = "{\"orderId\":1,\"status\":\"PROCESSED\",\"attemptedPaymentAt\":\"2024-11-01T19:35\"}";
        Message message = new Message(json.getBytes());
        listener.listen(message);

        verify(orderService).updateOrder(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getOrderId()).isEqualTo(1);
        assertThat(argumentCaptor.getValue().getStatus()).isEqualTo("PROCESSED");
        assertThat(argumentCaptor.getValue()
                .getAttemptedPaymentAt()).isEqualTo(LocalDateTime.of(2024,11,1,19, 35));

    }

    @Test
    void shouldSendToDeadWhenBusinessException() {
        String json = "{\"orderId\":1,\"status\":\"PROCESSED\",\"attemptedPaymentAt\":\"2024-11-01T19:35\"}";
        Message message = new Message(json.getBytes());
//        message.getMessageProperties().setHeader("x-retry-count", 0);

        doThrow(BusinessException.class).when(orderService).updateOrder(any());

        listener.listen(message);

        verify(rabbitTemplate).convertAndSend(exchange, dead, message);
    }

    @Test
    void shouldSendToDeadWhenInternalException() {
        String json = "{\"orderId\":1,\"status\":\"PROCESSED\",\"attemptedPaymentAt\":\"2024-11-01T19:35\"}";
        Message message = new Message(json.getBytes());

        doThrow(InternalException.class).when(orderService).updateOrder(any());

        listener.listen(message);

        verify(rabbitTemplate).convertAndSend(exchange, dead, message);
    }

    @Test
    void shouldSendToRetryWhenException() {
        String json = "{\"orderId\":1,\"status\":\"PROCESSED\",\"attemptedPaymentAt\":\"2024-11-01T19:35\"}";
        Message message = new Message(json.getBytes());

        doThrow(RuntimeException.class).when(orderService).updateOrder(any());

        listener.listen(message);

        verify(rabbitTemplate).convertAndSend(exchange, retry, message);
        var retryHeader = message.getMessageProperties().getHeader("x-retry-count");
        assertThat(retryHeader).isNotNull().isEqualTo(1);
    }

    @Test
    void shouldSendToDeadWhenMaxRetryIsReached() {
        String json = "{\"orderId\":1,\"status\":\"PROCESSED\",\"attemptedPaymentAt\":\"2024-11-01T19:35\"}";
        Message message = new Message(json.getBytes());
        message.getMessageProperties().setHeader("x-retry-count", 3);
        doThrow(RuntimeException.class).when(orderService).updateOrder(any());

        listener.listen(message);

        verify(rabbitTemplate).convertAndSend(exchange, dead, message);
        var retryHeader = message.getMessageProperties().getHeader("x-retry-count");
        assertThat(retryHeader).isNotNull().isEqualTo(3);
    }
}