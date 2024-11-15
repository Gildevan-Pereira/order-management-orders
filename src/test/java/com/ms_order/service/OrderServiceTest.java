package com.ms_order.service;

import com.ms_order.exception.BusinessException;
import com.ms_order.fixture.*;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import com.ms_order.model.mongodb.OrderHistoryDocument;
import com.ms_order.rabbitmq.CreateOrderPublisher;
import com.ms_order.model.dto.event.OrderCreatedDto;
import com.ms_order.repository.ItemRepository;
import com.ms_order.repository.OrderHistoryRepository;
import com.ms_order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
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
    @Captor
    private ArgumentCaptor<OrderCreatedDto> orderCreatedDtoCaptor;
    @Captor
    private ArgumentCaptor<OrderEntity> orderEntityCaptor;
    @Captor
    private ArgumentCaptor<OrderHistoryDocument> orderHistoryDocumentCaptor;

    @BeforeEach
    void setUp(){
        orderService = new OrderService(orderHistoryRepository, createOrderPublisher, orderRepository, itemRepository, modelMapper);
    }

    private final OrderItemDto itemDto1 = OrderItemDtoFixture.buildDefault(2, BigDecimal.valueOf(100.00));
    private final OrderItemDto itemDto2 = OrderItemDtoFixture.buildDefault(1, BigDecimal.valueOf(50.00));
    private final CreateOrderRequestDto requestDto = OrderRequestDtoFixture.buildDefault(List.of(itemDto1, itemDto2));
    private final OrderEntity orderEntity = OrderEntityFixture.buildFromRequestDto(requestDto);
    private final List<ItemEntity> itemsEntity = ItemEntityFixture.buildFromRequestDto(requestDto);

    private final OrderResponseDto responseDto = OrderResponseDtoFixture.buildFromEntity(orderEntity, List.of(itemDto1, itemDto2));
    private final OrderHistoryDocument orderHistory = OrderHistoryDocumentFixture.buildFromResponseDto(responseDto);
    private final List<ItemHistoryDocument> itemHistory = ItemHistoryDocumentFixture.buildFromEntity(itemsEntity);
    private final ItemHistoryDocument itemHistory1 = itemHistory.getFirst();
    private final ItemHistoryDocument itemHistory2 = itemHistory.getLast();

    private final OrderSearchFilterDto filterDto = OrderSearchFilterDtoFixture.buildDefault();

    @Test
    void shouldCreateOrderSuccessful() {

        when(modelMapper.map(requestDto, OrderEntity.class)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntityCaptor.capture()))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(modelMapper.map(itemDto1, ItemEntity.class)).thenReturn(itemsEntity.getFirst());
        when(modelMapper.map(itemDto2, ItemEntity.class)).thenReturn(itemsEntity.get(1));
        when(itemRepository.saveAll(itemsEntity)).thenReturn(itemsEntity);
        when(modelMapper.map(any(OrderEntity.class), eq(OrderResponseDto.class))).thenReturn(responseDto);
        when(modelMapper.map(itemsEntity.getFirst(), OrderItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(itemsEntity.get(1), OrderItemDto.class)).thenReturn(itemDto2);
        when(modelMapper.map(orderEntity, OrderHistoryDocument.class)).thenReturn(orderHistory);
        when(modelMapper.map(itemsEntity.getFirst(), ItemHistoryDocument.class)).thenReturn(itemHistory1);
        when(modelMapper.map(itemsEntity.getLast(), ItemHistoryDocument.class)).thenReturn(itemHistory2);
        when(orderHistoryRepository.save(orderHistoryDocumentCaptor.capture())).thenReturn(orderHistory);

        var response = orderService.createOrder(requestDto);

        verify(createOrderPublisher, times(1))
                .send(orderCreatedDtoCaptor.capture());

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(OrderResponseDto.class);
        assertThat(orderEntityCaptor.getValue().getStatus()).isEqualTo(OrderStatusEnum.CREATED);
        assertThat(orderEntityCaptor.getValue().getAmount()).isEqualTo(BigDecimal.valueOf(250.00));
        assertThat(response.getItems()).hasSize(2);
        assertThat(orderCreatedDtoCaptor.getValue().getOrderId()).isEqualTo(orderEntityCaptor.getValue().getId());
        assertThat(orderCreatedDtoCaptor.getValue().getAmount()).isEqualTo(orderEntityCaptor.getValue().getAmount());
        assertThat(orderHistoryDocumentCaptor.getValue().getItems()).isNotEmpty();

    }
    @Test
    void shouldReturnOrderWhenIdExists(){
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
        when(modelMapper.map(any(OrderEntity.class), eq(OrderResponseDto.class))).thenReturn(responseDto);

        var response = orderService.findById(1);

        verify(orderRepository, times(1)).findById(1);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(OrderResponseDto.class);
        assertThat(response.getId()).isEqualTo(1);
    }

    @Test
    void shouldThrowsBusinessExceptionWhenIdNotExists(){
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> orderService.findById(1));

        verify(modelMapper, never()).map(any(OrderEntity.class), eq(OrderResponseDto.class));
    }

    @Test
    void shouldFindByFiltersSuccessful(){

        var orders = List.of(orderEntity);

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<OrderEntity> orderPage = new PageImpl<>(orders);
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(orderPage);
        when(modelMapper.map(orderEntity, OrderResponseDto.class)).thenReturn(responseDto);

        var response = orderService.findByFilters(filterDto, pageable);

        verify(orderRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(PageImpl.class);
        assertThat(pageable.getPageSize()).isEqualTo(orders.size());
    }

    @Test
    void shouldUpdateOrderSuccessful() {
        var updatedDto = OrderUpdatedDtoFixture.buildDefault();
        orderEntity.setItems(itemsEntity);

        when(orderRepository.findById(updatedDto.getOrderId())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntityCaptor.capture()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        orderService.updateOrder(updatedDto);

        verify(orderHistoryRepository).save(orderHistoryDocumentCaptor.capture());

        assertThat(orderHistoryDocumentCaptor.getValue().getOrderId()).isEqualTo(updatedDto.getOrderId());
        assertThat(orderHistoryDocumentCaptor.getValue()
                .getStatus()).isEqualTo(OrderStatusEnum.valueOf(updatedDto.getStatus()));
        assertThat(orderHistoryDocumentCaptor.getValue().getAttemptedPaymentAt()).isEqualTo(updatedDto.getAttemptedPaymentAt());
    }

    @Test
    void shouldThrowsBusinessExceptionWhenStatusIsInvalid() {
        var updatedDto = OrderUpdatedDtoFixture.buildDefault();
        updatedDto.setStatus("INVALID");

        Executable executable = () -> orderService.updateOrder(updatedDto);

        assertThrows(BusinessException.class, executable);
    }

    @Test
    void shouldThrowsBusinessExceptionWhenOrderNotFound() {
        var updatedDto = OrderUpdatedDtoFixture.buildDefault();
        orderEntity.setItems(itemsEntity);

        when(orderRepository.findById(updatedDto.getOrderId())).thenReturn(Optional.empty());

        Executable executable = () -> orderService.updateOrder(updatedDto);

        assertThrows(BusinessException.class, executable);

    }

    @Test
    void shouldReturnHistoryByOrderId() {
        var historyDocument = List.of(OrderHistoryDocumentFixture.buildDefault());

        when(orderHistoryRepository.findByOrderId(historyDocument.getFirst().getOrderId())).thenReturn(historyDocument);

        var history = orderService.findOrderHistoryByOrderId(historyDocument.getFirst().getOrderId());

        verify(orderHistoryRepository).findByOrderId(historyDocument.getFirst().getOrderId());
        assertThat(history.size()).isEqualTo(historyDocument.size());
        assertThat(history.getFirst().getOrderId()).isEqualTo(historyDocument.getFirst().getOrderId());
        assertThat(history.getFirst().getItems().size()).isEqualTo(historyDocument.getFirst().getItems().size());
    }

    @Test
    void shouldThrowsBusinessExceptionWhenHistoryNotFound() {

        when(orderHistoryRepository.findByOrderId(anyInt())).thenReturn(Collections.emptyList());

        Executable executable = () -> orderService.findOrderHistoryByOrderId(1);

        assertThrows(BusinessException.class, executable);
    }
}
