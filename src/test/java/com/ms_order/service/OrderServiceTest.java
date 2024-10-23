package com.ms_order.service;

import com.ms_order.fixure.OrdemItemDtoFixure;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import com.ms_order.repository.ItemRepository;
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

    @BeforeEach
    void setUp(){
        orderService = new OrderService(createOrderPublisher, orderRepository, itemRepository, modelMapper);
    }

    @Test
    void shouldCreateOrderSuccessful(){

        var itemDto1 = OrdemItemDtoFixure.buildDefault(2, BigDecimal.valueOf(100.00));
        var itemDto2 = OrdemItemDtoFixure.buildDefault(1, BigDecimal.valueOf(50.00));

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .name("Jon Doe")
                .cpf("12345678901")
                .address("Rua x")
                .postalCode("12345678")
                .city("Cidade x")
                .state("SP")
                .items(List.of(itemDto1, itemDto2))
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1)
                .name(requestDto.getName())
                .cpf(requestDto.getCpf())
                .address(requestDto.getAddress())
                .postalCode(requestDto.getPostalCode())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .build();

        ItemEntity itemEntity1 = ItemEntity.builder()
                .name(itemDto1.getName())
                .description(itemDto1.getDescription())
                .unityPrice(itemDto1.getUnityPrice())
                .count(itemDto1.getCount())
                .build();

        ItemEntity itemEntity2 = ItemEntity.builder()
                .name(itemDto2.getName())
                .description(itemDto2.getDescription())
                .unityPrice(itemDto2.getUnityPrice())
                .count(itemDto2.getCount())
                .build();

        CreateOrderResponseDto responseDto = CreateOrderResponseDto.builder()
                .status(orderEntity.getStatus())
                .name(orderEntity.getName())
                .cpf(orderEntity.getCpf())
                .address(orderEntity.getAddress())
                .postalCode(orderEntity.getPostalCode())
                .city(orderEntity.getCity())
                .state(orderEntity.getState())
                .items(List.of(itemDto1, itemDto2))
                .build();

        when(modelMapper.map(requestDto, OrderEntity.class)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntityCaptor.capture()))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(modelMapper.map(itemDto1, ItemEntity.class)).thenReturn(itemEntity1);
        when(modelMapper.map(itemDto2, ItemEntity.class)).thenReturn(itemEntity2);
        when(itemRepository.saveAll(List.of(itemEntity1, itemEntity2))).thenReturn(List.of(itemEntity1, itemEntity2));
        when(modelMapper.map(any(OrderEntity.class), eq(CreateOrderResponseDto.class))).thenReturn(responseDto);
        when(modelMapper.map(itemEntity1, OrderItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(itemEntity2, OrderItemDto.class)).thenReturn(itemDto2);
//        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderCreatedDto.class));

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


    }
}