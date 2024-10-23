package com.ms_order.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_order.exception.ErrorToParseStringException;
import com.ms_order.rabbitmq.dto.CreateOrderPublisherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Queue createOrderQueue;

    @Value("${mq.exchanges.order_exchange}")
    private String orderExchange;

    @Value("${mq.routing-keys.order_routing_key}")
    private String orderRoutingKey;


    public void createOrderPublisher(CreateOrderPublisherDto dto) {
        var stringOrder = convertObjectToString(dto);
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, stringOrder);
    }

    public String convertObjectToString(CreateOrderPublisherDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new ErrorToParseStringException(e.getMessage());
        }
    }

}
