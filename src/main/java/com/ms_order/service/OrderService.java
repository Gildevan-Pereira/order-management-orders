package com.ms_order.service;

import com.ms_order.exception.BusinessException;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.dto.response.OrderHistoryResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mappers.OrderEntityMapper;
import com.ms_order.model.mappers.OrderHistoryMapper;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.model.dto.event.OrderCreatedDto;
import com.ms_order.model.dto.event.OrderUpdatedDto;
import com.ms_order.repository.ItemRepository;
import com.ms_order.repository.OrderHistoryRepository;
import com.ms_order.repository.OrderRepository;
import com.ms_order.specification.OrderSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final CreateOrderPublisher createOrderPublisher;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {

        var orderEntity = modelMapper.map(requestDto, OrderEntity.class);
        orderEntity.setStatus(OrderStatusEnum.CREATED);

        var amount = requestDto.getItems().stream()
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getUnityPrice().multiply(BigDecimal.valueOf(b.getCount()))),
                        BigDecimal::add);

        orderEntity.setAmount(amount);

        var orderSaved = orderRepository.save(orderEntity);
        log.info("OrderService.createOrder - Order created successful | orderId: {}", orderSaved.getId());

        List<ItemEntity> items = requestDto.getItems().stream()
                .map(item -> modelMapper.map(item, ItemEntity.class))
                .toList();

        items.forEach(item -> item.setOrder(orderSaved.getId()));

        var itemsSaved = itemRepository.saveAll(items);
        log.info("OrderService.createOrder - Order items created successful | orderId: {} | count: {}",
                orderSaved.getId(), items.size());

        var orderResponse = modelMapper.map(orderSaved, OrderResponseDto.class);
        var orderItemResponse = itemsSaved.stream()
                .map(itemEntity -> modelMapper.map(itemEntity, OrderItemDto.class))
                .toList();

        orderResponse.setItems(orderItemResponse);

        var createOrderEvent = OrderCreatedDto.builder()
                                    .orderId(orderSaved.getId())
                                    .amount(orderSaved.getAmount())
                                    .build();

        createOrderPublisher.send(createOrderEvent);

        OrderHistoryDocument orderHistory = modelMapper.map(orderSaved, OrderHistoryDocument.class);
        var itemHistory = itemsSaved.stream()
                .map(itemEntity -> modelMapper.map(itemEntity, ItemHistoryDocument.class))
                .toList();
        orderHistory.setItems(itemHistory);

        var orderHistorySaved = orderHistoryRepository.save(orderHistory);
        log.info("OrderService.createOrder - Order history created successful | orderId: {} | orderHistoryId: {}",
                orderSaved.getId(), orderHistorySaved.getId());

        return orderResponse;
    }

    public OrderResponseDto findById(Integer id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        MessageEnum.ORDER_NOT_FOUND, id.toString(), HttpStatus.NOT_FOUND));
        log.info("OrderService.findById - Order request found | id: {}", id);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    public Page<OrderResponseDto> findByFilters(OrderSearchFilterDto filterDto, Pageable pageable) {
        log.debug("OrderService.findByFilters - Filter order request received | filters: {}", filterDto);

        var ordersPage = orderRepository.findAll(OrderSpecification.filterTo(filterDto), pageable);
        log.debug("OrderService.findByFilters - Orders found for filters | count: {}", ordersPage.getTotalElements());
        var orders = ordersPage.getContent().stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class)).toList();
        return new PageImpl<>(orders, pageable, ordersPage.getTotalElements());
    }

    @Transactional
    public void updateOrder(OrderUpdatedDto updatedDto) {

        var status = OrderStatusEnum.fromName(updatedDto.getStatus());

        var order = orderRepository.findById(updatedDto.getOrderId())
                .orElseThrow(() -> new BusinessException(
                        MessageEnum.ORDER_NOT_FOUND, updatedDto.getOrderId().toString(), HttpStatus.NOT_FOUND));

        order.setStatus(status);
        order.setAttemptedPaymentAt(updatedDto.getAttemptedPaymentAt());

        var orderUpdated = orderRepository.save(order);
        log.info("OrderService.updateOrder - Order updated | status: {}", orderUpdated.getStatus());

        OrderHistoryDocument orderHistory = OrderEntityMapper.toDocument(orderUpdated);

        orderHistoryRepository.save(orderHistory);
        log.info("OrderService.updateOrder - Order history updated | status: {}", orderUpdated.getStatus());

    }

    public List<OrderHistoryResponseDto> findOrderHistoryByOrderId(Integer id) {

        List<OrderHistoryDocument> history = orderHistoryRepository.findByOrderId(id);

        if (history.isEmpty()) {
            throw new BusinessException(MessageEnum.ORDER_HISTORY_NOT_FOUND, id.toString(), HttpStatus.NOT_FOUND);
        }

        log.debug("OrderService.findOrderHistoryByOrderId - Order history find for id: {} | count: {}", id, history.size());
        return history.stream().map(OrderHistoryMapper::toResponseDto).toList();
    }
}
