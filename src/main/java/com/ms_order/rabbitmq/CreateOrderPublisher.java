package com.ms_order.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_order.exception.ErrorToParseStringException;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mq.exchanges.order_exchange}")
    private String orderExchange;

    @Value("${mq.routing-keys.order_routing_key}")
    private String orderRoutingKey;

    public void send(OrderCreatedDto dto) {
        try {
            var converted = convertObjectToString(dto);
            rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, converted);
        } catch (AmqpException e) {
            log.error("CreateOrderPublisher.send - Error while sending message", e);
        }
    }

    public String convertObjectToString(OrderCreatedDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new ErrorToParseStringException(e.getMessage());
        }
    }


}
