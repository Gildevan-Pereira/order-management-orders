package com.ms_order.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${mq.queues.create_order_queue}")
    private String createOrderQueue;

    @Value("${mq.exchanges.order_exchange}")
    private String order_exchange;

    @Value("${mq.routing-keys.order_routing_key}")
    private String order_routing_key;

    @Bean
    public Queue createOrderQueue() {
        return new Queue(createOrderQueue, true);
    }

    @Bean
    public TopicExchange order_exchange() {
        return new TopicExchange(order_exchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(createOrderQueue())
                .to(order_exchange())
                .with(order_routing_key);
    }
}
