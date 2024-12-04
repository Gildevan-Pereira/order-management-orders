package com.ms_order.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

public abstract class BaseListener {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String retryRoutingKey;
    private final String deadRoutingKey;
    private final Integer maxRetry;

    protected BaseListener(RabbitTemplate rabbitTemplate, String exchange,
                           String retryRoutingKey, String deadRoutingKey, Integer maxRetry){
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.retryRoutingKey = retryRoutingKey;
        this.deadRoutingKey = deadRoutingKey;
        this.maxRetry = maxRetry;
    }

    protected void sendToRetry(Message message) {
        var retryCount = Optional.ofNullable(
                (Integer) message.getMessageProperties().getHeaders().get("x-retry-count")).orElse(0);

        if (retryCount >= maxRetry) {
            sendToDead(message);
            return;
        }

        message.getMessageProperties().setHeader("x-retry-count", retryCount + 1);
        rabbitTemplate.convertAndSend(exchange, retryRoutingKey, message);
    }

    protected void sendToDead(Message message) {
        rabbitTemplate.convertAndSend(exchange, deadRoutingKey, message);
    }

    public abstract void listen(Message message);
}
