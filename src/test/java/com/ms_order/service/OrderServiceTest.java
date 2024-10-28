package com.ms_order.service;

import com.ms_order.fixure.*;
import com.ms_order.model.dto.request.OrderItemDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderHistoryRepository ordemHistoryRepository;
    @Mock
    private CreateOrderPublisher createOrderPublisher;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Captor
    private ArgumentCaptor<OrderCreatedDto> orderCreatedDtoCaptor;
    @Captor
    private ArgumentCaptor<OrderEntity> orderEntityCaptor;
    @Captor
    private ArgumentCaptor<OrderHistoryEntity> orderHistoryEntityCaptor;

    @BeforeEach
    void setUp(){
        orderService = new OrderService(ordemHistoryRepository, createOrderPublisher, orderRepository, itemRepository, modelMapper);
    }

    @Test
    void shouldCreateOrderSuccessful(){

        var itemDto1 = OrdemItemDtoFixure.buildDefault(2, BigDecimal.valueOf(100.00));
        var itemDto2 = OrdemItemDtoFixure.buildDefault(1, BigDecimal.valueOf(50.00));
        var requestDto = CreateOrderRequestDtoFixure.buildDefault(List.of(itemDto1, itemDto2));
        var orderEntity = OrderEntityFixure.buildDefault(requestDto);
        var itemEntity = ItemEntityFixure.buildDefault(requestDto);

        var responseDto = CreateOrderResponseDtoFixure.buildDefault(orderEntity, List.of(itemDto1, itemDto2));
        var orderHistory = OrderHistoryEntityFixure.buildeDefault(responseDto);
        var itemHistory = ItemHistoryEntityFixure.buildDefault(itemEntity);
        var itemHistory1 = itemHistory.getFirst();
        var itemHistory2 = itemHistory.getLast();
        when(modelMapper.map(requestDto, OrderEntity.class)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntityCaptor.capture()))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(modelMapper.map(itemDto1, ItemEntity.class)).thenReturn(itemEntity.getFirst());
        when(modelMapper.map(itemDto2, ItemEntity.class)).thenReturn(itemEntity.get(1));
        when(itemRepository.saveAll(itemEntity)).thenReturn(itemEntity);
        when(modelMapper.map(any(OrderEntity.class), eq(CreateOrderResponseDto.class))).thenReturn(responseDto);
        when(modelMapper.map(itemEntity.getFirst(), OrderItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(itemEntity.get(1), OrderItemDto.class)).thenReturn(itemDto2);
        when(modelMapper.map(orderEntity, OrderHistoryEntity.class)).thenReturn(orderHistory);
        when(modelMapper.map(itemEntity.getFirst(), ItemHistoryEntity.class)).thenReturn(itemHistory1);
        when(modelMapper.map(itemEntity.getLast(), ItemHistoryEntity.class)).thenReturn(itemHistory2);
        when(ordemHistoryRepository.save(orderHistoryEntityCaptor.capture())).thenReturn(orderHistory);

        var response = orderService.createOrder(requestDto);

        verify(createOrderPublisher, times(1))
                .send(orderCreatedDtoCaptor.capture());

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(CreateOrderResponseDto.class);
        assertThat(orderEntityCaptor.getValue().getStatus()).isEqualTo(OrderStatusEnum.CREATED);
        assertThat(orderEntityCaptor.getValue().getAmount()).isEqualTo(BigDecimal.valueOf(250.00));
        assertThat(response.getItems()).hasSize(2);
        assertThat(orderCreatedDtoCaptor.getValue().getOrderId()).isEqualTo(orderEntityCaptor.getValue().getId());
        assertThat(orderCreatedDtoCaptor.getValue().getAmount()).isEqualTo(orderEntityCaptor.getValue().getAmount());
        assertThat(orderHistoryEntityCaptor.getValue().getId()).isNull();
        assertThat(orderHistoryEntityCaptor.getValue().getCreatedAt()).isNotNull();
        assertThat(orderHistoryEntityCaptor.getValue().getItems()).isNotNull();

    }
}