package com.ms_order.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateOrderRabbitMQConfigTest {

    private UpdateOrderRabbitMQConfig mqConfig;

    private final String exchange = "exchange";
    private final String routingKey = "routingKey";
    private final String queue = "queue";
    private final String retryQueue = "retryQueue";
    private final String deadQueue = "deadQueue";

    @BeforeEach
    void setUp() {
        mqConfig = new UpdateOrderRabbitMQConfig(exchange, routingKey, queue, retryQueue, deadQueue);
    }

    @Test
    void createExchange() {
       var result = mqConfig.createExchange();
       assertThat(result).isNotNull().isInstanceOf(TopicExchange.class);
       assertThat(result.getName()).isEqualTo(exchange);
    }

    @Test
    void queue() {
        var result = mqConfig.queue();
        assertThat(result).isInstanceOf(Queue.class);
        assertThat(result.getName()).isEqualTo(queue);
    }

    @Test
    void retryQueue() {
        var result = mqConfig.retryQueue();
        assertThat(result).isNotNull().isInstanceOf(Queue.class);
        assertThat(result.getName()).isEqualTo(retryQueue);
        assertThat(result.getArguments().get("x-dead-letter-exchange")).isEqualTo(exchange);
        assertThat(result.getArguments().get("x-dead-letter-routing-key")).isEqualTo(routingKey);
        assertThat(result.getArguments().get("x-message-ttl")).isEqualTo(5);
    }

    @Test
    void deadQueue() {
        var result = mqConfig.deadQueue();
        assertThat(result).isNotNull().isInstanceOf(Queue.class);
        assertThat(result.getName()).isEqualTo(deadQueue);
    }

    @Test
    void queueBinding() {
        var result = mqConfig.queueBinding();
        assertThat(result).isNotNull().isInstanceOf(Binding.class);
    }

    @Test
    void retryQueueBinding() {
        var result = mqConfig.retryQueueBinding();
        assertThat(result).isNotNull().isInstanceOf(Binding.class);
    }

    @Test
    void deadQueueBinding() {
        var result = mqConfig.deadQueueBinding();
        assertThat(result).isNotNull().isInstanceOf(Binding.class);
    }
}