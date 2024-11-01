package com.ms_order.rabbitmq;

import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import com.ms_order.util.JsonParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchanges.created_order_exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routing_keys.created_order_routing_key}")
    private String routingKey;

    public void send(OrderCreatedDto dto) {
        try {
            var converted = JsonParserUtil.toJson(dto);
            rabbitTemplate.convertAndSend(exchange, routingKey, converted);
            log.info("CreateOrderPublisher.send - Order created event sent | data: {}", converted);
        } catch (Exception e) {

            log.error("CreateOrderPublisher.send - Error while sending message", e);
        }
    }
}
