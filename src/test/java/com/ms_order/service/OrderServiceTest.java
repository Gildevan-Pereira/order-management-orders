package com.ms_order.service;

import com.ms_order.exception.BusinessException;
import com.ms_order.fixure.*;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.rabbitmq.dto.OrderCreatedDto;
import com.ms_order.repository.ItemRepository;
import com.ms_order.repository.OrderHistoryRepository;
import com.ms_order.repository.OrderRepository;
import com.ms_order.specification.OrderSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;
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
    private ArgumentCaptor<OrderHistoryDocument> orderHistoryEntityCaptor;

    @BeforeEach
    void setUp(){
        orderService = new OrderService(orderHistoryRepository, createOrderPublisher, orderRepository, itemRepository, modelMapper);
    }
    private final OrderItemDto itemDto1 = OrdemItemDtoFixure.buildDefault(2, BigDecimal.valueOf(100.00));
    private final OrderItemDto itemDto2 = OrdemItemDtoFixure.buildDefault(1, BigDecimal.valueOf(50.00));
    private final CreateOrderRequestDto requestDto = CreateOrderRequestDtoFixure.buildDefault(List.of(itemDto1, itemDto2));
    private final OrderEntity orderEntity = OrderEntityFixure.buildDefault(requestDto);
    private final List<ItemEntity> itemEntity = ItemEntityFixure.buildDefault(requestDto);

    private final CreateOrderResponseDto responseDto = CreateOrderResponseDtoFixure.buildDefault(orderEntity, List.of(itemDto1, itemDto2));
    private final OrderHistoryDocument orderHistory = OrderHistoryDocumentFixure.buildeDefault(responseDto);
    private final List<ItemHistoryDocument> itemHistory = ItemHistoryDocumentFixure.buildDefault(itemEntity);
    private final ItemHistoryDocument itemHistory1 = itemHistory.getFirst();
    private final ItemHistoryDocument itemHistory2 = itemHistory.getLast();

    private final OrderSearchFilterDto filterDto = OrderSearchFilterDtoFixture.buildDefault();

    @Test
    void shouldCreateOrderSuccessful() {

        when(modelMapper.map(requestDto, OrderEntity.class)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntityCaptor.capture()))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(modelMapper.map(itemDto1, ItemEntity.class)).thenReturn(itemEntity.getFirst());
        when(modelMapper.map(itemDto2, ItemEntity.class)).thenReturn(itemEntity.get(1));
        when(itemRepository.saveAll(itemEntity)).thenReturn(itemEntity);
        when(modelMapper.map(any(OrderEntity.class), eq(CreateOrderResponseDto.class))).thenReturn(responseDto);
        when(modelMapper.map(itemEntity.getFirst(), OrderItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(itemEntity.get(1), OrderItemDto.class)).thenReturn(itemDto2);
        when(modelMapper.map(orderEntity, OrderHistoryDocument.class)).thenReturn(orderHistory);
        when(modelMapper.map(itemEntity.getFirst(), ItemHistoryDocument.class)).thenReturn(itemHistory1);
        when(modelMapper.map(itemEntity.getLast(), ItemHistoryDocument.class)).thenReturn(itemHistory2);
        when(orderHistoryRepository.save(orderHistoryEntityCaptor.capture())).thenReturn(orderHistory);

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
        assertThat(orderHistoryEntityCaptor.getValue().getItems()).isNotEmpty();

    }
    @Test
    void shouldFindByIdWhenIdExists(){
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
        when(modelMapper.map(any(OrderEntity.class), eq(CreateOrderResponseDto.class))).thenReturn(responseDto);

        var response = orderService.findById(1);

        verify(orderRepository, times(1)).findById(1);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(CreateOrderResponseDto.class);
        assertThat(orderEntity).isNotNull();
        assertThat(responseDto).isNotNull();
    }

    @Test
    void shouldFindByIdWhenIdNotExists(){
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            orderService.findById(1);
        });

        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void shouldFindByFiltersSuccessful(){
        var specification = OrderSpecification.filterTo(filterDto);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<OrderEntity> orderPage = new PageImpl<>(List.of(orderEntity));
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(orderPage);
//        when(orderPage.stream().map(orderEntity -> any())).thenReturn(Stream.of(responseDto));
        var orderConverted = when(modelMapper.map(orderEntity, CreateOrderResponseDto.class)).thenReturn(responseDto);

        var response = orderService.findByFilters(filterDto, pageable);

        verify(orderRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(PageImpl.class);
        assertThat(pageable.getPageSize()).isNotZero();
        assertThat(filterDto).isNotNull();

    }
}
