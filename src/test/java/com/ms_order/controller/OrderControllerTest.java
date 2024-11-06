package com.ms_order.controller;

import com.ms_order.exception.ApiExceptionHandler;
import com.ms_order.exception.BusinessException;
import com.ms_order.fixture.OrderHistoryDocumentFixture;
import com.ms_order.fixture.OrderItemDtoFixture;
import com.ms_order.fixture.OrderRequestDtoFixture;
import com.ms_order.fixture.OrderResponseDtoFixture;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.model.mappers.OrderHistoryMapper;
import com.ms_order.service.OrderService;
import com.ms_order.util.JsonParserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    private static Stream<Arguments> provideParams(){
        OrderItemDto item1 = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", BigDecimal.valueOf(100.0),1);
        OrderItemDto itemInvalidName = OrderItemDtoFixture.buildFromParams("x", "Descrição", BigDecimal.valueOf(100.0),1);
        OrderItemDto itemInvalidDescription = OrderItemDtoFixture.buildFromParams("Item 1", null, BigDecimal.valueOf(100.0),1);
        OrderItemDto itemUnityPriceNull = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", null,1);
        OrderItemDto itemInvalidCount = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", BigDecimal.valueOf(100.0),0);

        CreateOrderRequestDto invalidName = OrderRequestDtoFixture.buildFromParams(
                "123", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidCpf = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "abc", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidAddress = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "x", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidPostalCode = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", null, "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidCity = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidState = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "12", List.of(item1));
        CreateOrderRequestDto invalidItemName = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemInvalidName));
        CreateOrderRequestDto invalidItemDescription = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemInvalidDescription));
        CreateOrderRequestDto invalidItemUnityPrice = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemUnityPriceNull));
        CreateOrderRequestDto invalidItemCount = OrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemInvalidCount));

        return Stream.of(
                Arguments.of(invalidName, MessageEnum.CLIENT_NAME_INVALID),
                Arguments.of(invalidCpf, MessageEnum.CPF_INVALID),
                Arguments.of(invalidAddress, MessageEnum.ADDRESS_INVALID),
                Arguments.of(invalidPostalCode, MessageEnum.POSTAL_CODE_INVALID),
                Arguments.of(invalidCity, MessageEnum.CITY_INVALID),
                Arguments.of(invalidState, MessageEnum.STATE_INVALID),
                Arguments.of(invalidItemName, MessageEnum.ITEM_NAME_INVALID),
                Arguments.of(invalidItemDescription, MessageEnum.ITEM_DESCRIPTION_INVALID),
                Arguments.of(invalidItemUnityPrice, MessageEnum.ITEM_UNITY_PRICE_INVALID),
                Arguments.of(invalidItemCount, MessageEnum.ITEM_COUNT_INVALID)
        );
    }

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
                .setControllerAdvice(new ApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }


    @Test
    void createOrder() throws Exception {
        var item1 = OrderItemDtoFixture.buildDefault(2, BigDecimal.valueOf(100));
        var item2 = OrderItemDtoFixture.buildDefault(1, BigDecimal.valueOf(50));
        var request = OrderRequestDtoFixture.buildDefault(List.of(item1, item2));
        var responseDto = OrderResponseDtoFixture.buildDefault();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orders")
                .content(JsonParserUtil.toJson(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        when(orderService.createOrder(request)).thenReturn(responseDto);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.id", is(1)));
        response.andExpect(jsonPath("$.status", is("CREATED")));
    }

    @ParameterizedTest
    @MethodSource("provideParams")
    void createOrderError(CreateOrderRequestDto request, MessageEnum messageEnum) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orders")
                .content(JsonParserUtil.toJson(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.errors[0].code", is(messageEnum.getCode())));
        response.andExpect(jsonPath("$.errors[0].message", is(messageEnum.getMessage())));
    }

    @Test
    void getOrderByIdSuccessful() throws Exception {
        Integer id = 1;
        var responseDto = OrderResponseDtoFixture.buildDefault();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/{id}", id);

        when(orderService.findById(id)).thenReturn(responseDto);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id", is(id)));
    }

    @Test
    void getOrderByIdError() throws Exception {
        Integer id = 1;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/{id}", id);

        when(orderService.findById(id))
                .thenThrow(new BusinessException(MessageEnum.ORDER_NOT_FOUND, id.toString(), HttpStatus.NOT_FOUND));

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.errors[0].code", is(MessageEnum.ORDER_NOT_FOUND.getCode())));
        response.andExpect(jsonPath("$.errors[0].message",
                                is(String.format(MessageEnum.ORDER_NOT_FOUND.getMessage(), id))));
    }

    @Test
    void findByFilterSuccessful() throws Exception {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        var responseDto = OrderResponseDtoFixture.buildDefault();
        Page<OrderResponseDto> orderPage = new PageImpl<>(List.of(responseDto), pageable, pageable.getPageSize());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ids", "1,2,3");
        params.add("page", "0");
        params.add("size", "1");
        params.add("sort", "id,ASC");

        when(orderService.findByFilters(any(OrderSearchFilterDto.class), eq(pageable))).thenReturn(orderPage);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders")
                .params(params);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(jsonPath("$.content[0].id", is(1)));
        response.andExpect(jsonPath("$.pageable.pageNumber", is(pageable.getPageNumber())));
        response.andExpect(jsonPath("$.pageable.pageSize", is(pageable.getPageSize())));
        response.andExpect(jsonPath("$.pageable.sort.sorted", is(true)));
    }

    @Test
    void findByFilterError() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", "INVALID");

        String statusList = Arrays.asList(OrderStatusEnum.values()).toString();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders")
                .params(params);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.errors[0].code", is(MessageEnum.INVALID_STATUS.getCode())));
        response.andExpect(jsonPath("$.errors[0].message",
                is(String.format(MessageEnum.INVALID_STATUS.getMessage(), statusList))));
    }

    @Test
    void findOrderHistoryByOrderId() throws Exception {
        Integer id = 1;
        var responseDto = OrderHistoryMapper.toResponseDto(OrderHistoryDocumentFixture.buildDefault());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/history/{id}", id);

        when(orderService.findOrderHistoryByOrderId(id)).thenReturn(List.of(responseDto));

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.length()", is(1)));
        response.andExpect(jsonPath("$.[0].id", notNullValue()));
        response.andExpect(jsonPath("$.[0].orderId", is(1)));
        response.andExpect(jsonPath("$.[0].status", is("CREATED")));
    }

    @Test
    void findOrderHistoryByOrderIdError() throws Exception {
        Integer id = 1;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/history/{id}", id);

        when(orderService.findOrderHistoryByOrderId(id))
                .thenThrow(new BusinessException(MessageEnum.ORDER_HISTORY_NOT_FOUND, id.toString(), HttpStatus.NOT_FOUND));

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.errors[0].code", is(MessageEnum.ORDER_HISTORY_NOT_FOUND.getCode())));
        response.andExpect(jsonPath("$.errors[0].message",
                is(String.format(MessageEnum.ORDER_HISTORY_NOT_FOUND.getMessage(), id))));
    }
}