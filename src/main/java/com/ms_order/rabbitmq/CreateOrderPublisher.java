package com.ms_order.rabbitmq;

import com.ms_order.exception.InternalException;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.event.OrderCreatedDto;
import com.ms_order.util.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final  String exchange;

    private final String routingKey;

    public CreateOrderPublisher(RabbitTemplate rabbitTemplate,
                                @Value("${spring.rabbitmq.exchanges.order_management_events}")
                                String exchange,
                                @Value("${spring.rabbitmq.routing_keys.created_order_routing_key}")
                                String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(OrderCreatedDto dto) {
        try {
            var json = JsonParserUtil.toJson(dto);
            rabbitTemplate.convertAndSend(exchange, routingKey, json);
            log.info("CreateOrderPublisher.send - Order created event sent | data: {}", json);
        } catch (Exception e) {
            log.error("CreateOrderPublisher.send - Error while sending message", e);
            throw new InternalException(MessageEnum.ERROR_PUBLISHER, HttpStatus.BAD_GATEWAY);
        }
    }
}
