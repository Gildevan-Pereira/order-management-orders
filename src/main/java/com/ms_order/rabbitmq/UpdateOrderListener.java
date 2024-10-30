package com.ms_order.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderListener {

    @RabbitListener(queues = "${mq.queues.update_order_queue}")
    public void updateOrderListener(@Payload String payload) {

        System.out.println(payload);
    }
}
