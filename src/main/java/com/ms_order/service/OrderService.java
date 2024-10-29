package com.ms_order.service;

import com.ms_order.exception.BusinessException;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mongodb.ItemHistoryEntity;
import com.ms_order.model.mongodb.OrderHistoryEntity;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto) {

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

        OrderHistoryEntity orderHistory = modelMapper.map(orderSaved, OrderHistoryEntity.class);
        var itemHistory = itemsSaved.stream()
                .map(itemEntity -> modelMapper.map(itemEntity, ItemHistoryEntity.class))
                .toList();
        orderHistory.setItems(itemHistory);

        var orderHistorySaved = orderHistoryRepository.save(orderHistory);
        log.info("OrderService.createOrder - Order history created successful | orderId: {} | orderHistoryId: {}",
                orderSaved.getId(), orderHistorySaved.getUuid());

        return orderResponse;
    }

    public CreateOrderResponseDto findById(Integer id) {
        var order = Optional.ofNullable(orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Order not find for id: " + id)));
        return modelMapper.map(order, CreateOrderResponseDto.class);
    }

//    TODO: Capturar exceção MethodArgumentNotValidException para o campo de status
    public Page<CreateOrderResponseDto> findByFilters(OrderSearchFilterDto filterDto, Pageable pageable) {

        if (!OrderStatusEnum.isValid(filterDto.getStatus())) {
            throw new BusinessException("Invalid status given. Accepted values: " + OrderStatusEnum.getAcceptedValues());
        }

        var ordersPage = orderRepository.findAll(OrderSpecification.filterTo(filterDto), pageable);
        var orders = ordersPage.getContent().stream()
                .map(order -> modelMapper.map(order, CreateOrderResponseDto.class)).toList();
        return new PageImpl<>(orders, pageable, ordersPage.getTotalElements());
    }

}
