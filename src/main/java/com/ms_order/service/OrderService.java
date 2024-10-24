package com.ms_order.service;

import com.ms_order.model.builder.OrderHistoryEntityBuilder;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mongodb.OrderHistoryEntity;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import com.ms_order.repository.ItemRepository;
import com.ms_order.repository.OrdemHistoryRepository;
import com.ms_order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdemHistoryRepository ordemHistoryRepository;
    private final CreateOrderPublisher createOrderPublisher;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto) {

        var orderEntity = modelMapper.map(requestDto, OrderEntity.class);
        orderEntity.setStatus(OrderStatusEnum.CREATED);

        var amount = requestDto.getItems().stream()
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getUnityPrice().multiply(BigDecimal.valueOf(b.getCount()))),
                        BigDecimal::add);

        orderEntity.setAmount(amount);

        var orderSaved = orderRepository.save(orderEntity);

        List<ItemEntity> items = requestDto.getItems().stream()
                .map(item -> modelMapper.map(item, ItemEntity.class))
                .toList();

        items.forEach(item -> item.setOrder(orderSaved));

        var itemsSaved = itemRepository.saveAll(items);

        var orderResponse = modelMapper.map(orderSaved, CreateOrderResponseDto.class);
        var orderItemResponse = itemsSaved.stream()
                .map(itemEntity -> modelMapper.map(itemEntity, OrderItemDto.class))
                .toList();

        orderResponse.setItems(orderItemResponse);

        var createOrderEvent = OrderCreatedDto.builder()
                                    .orderId(orderSaved.getId())
                                    .amount(orderSaved.getAmount())
                                    .build();

        createOrderPublisher.send(createOrderEvent);

//        OrderHistoryEntity orderHistory = OrderHistoryEntityBuilder.build(orderResponse);
        OrderHistoryEntity orderHistory = modelMapper.map(orderResponse, OrderHistoryEntity.class);
        orderHistory.setId(null);
        orderHistory.setCreatedAt(LocalDateTime.now());

        var historySaved = ordemHistoryRepository.save(orderHistory);

        return orderResponse;
    }

}
