package com.ms_order.rabbitmq;

import com.ms_order.exception.BusinessException;
import com.ms_order.exception.InternalException;
import com.ms_order.model.dto.event.OrderUpdatedDto;
import com.ms_order.service.OrderService;
import com.ms_order.util.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateOrderListener extends BaseListener {

    private final OrderService orderService;

    protected UpdateOrderListener(
            OrderService orderService,
            RabbitTemplate rabbitTemplate,
            @Value("${spring.rabbitmq.exchanges.order_management_events}")
            String exchange,
            @Value("${spring.rabbitmq.queues.update_order.retry_queue}")
            String retryRoutingKey,
            @Value("${spring.rabbitmq.queues.update_order.dead_queue}")
            String deadRoutingKey,
            @Value("${spring.rabbitmq.max_retry}")
            Integer maxRetry) {

        super(rabbitTemplate, exchange, retryRoutingKey, deadRoutingKey, maxRetry);
        this.orderService = orderService;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.update_order.queue}")
    public void listen(Message message) {

        try {
            var orderUpdatedDto = JsonParserUtil.fromBytes(message.getBody(), OrderUpdatedDto.class);

            log.info("UpdateOrderListener.listen - Data to update order received | data: {}", orderUpdatedDto);

            orderService.updateOrder(orderUpdatedDto);

        } catch (InternalException | BusinessException e) {
            log.error("UpdateOrderListener.listen - An error has occurred | error: {}", e.getMessage(), e);
            super.sendToDead(message);
        } catch (Exception e) {
            log.error("UpdateOrderListener.listen - An error has occurred | error: {}", e.getMessage(), e);
            super.sendToRetry(message);
        }
    }
}
