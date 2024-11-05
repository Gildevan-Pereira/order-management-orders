package com.ms_order.controller;

import com.ms_order.exception.ApiExceptionHandler;
import com.ms_order.fixture.CreateOrderRequestDtoFixture;
import com.ms_order.fixture.OrderItemDtoFixture;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderItemDto;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    private final OrderItemDto item = OrderItemDtoFixture.buildDefault(1, BigDecimal.valueOf(100.0));
    private final CreateOrderRequestDto request = CreateOrderRequestDtoFixture.buildDefault(List.of(item));

    private static Stream<Arguments> errorInputs(){
        OrderItemDto item1 = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", BigDecimal.valueOf(100.0),1);
        OrderItemDto itemInvalidName = OrderItemDtoFixture.buildFromParams("x", "Descrição", BigDecimal.valueOf(100.0),1);
        OrderItemDto itemInvalidDescription = OrderItemDtoFixture.buildFromParams("Item 1", null, BigDecimal.valueOf(100.0),1);
        OrderItemDto itemUnityPriceNull = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", null,1);
        OrderItemDto itemInvalidCount = OrderItemDtoFixture.buildFromParams("Item 1", "Descrição", BigDecimal.valueOf(100.0),0);

        CreateOrderRequestDto invalidName = CreateOrderRequestDtoFixture.buildFromParams(
                "123", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidCpf = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "abc", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidAddress = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "x", "12345678", "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidPostalCode = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", null, "Cidade X",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidCity = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "",
                "SP", List.of(item1));
        CreateOrderRequestDto invalidState = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "12", List.of(item1));
        CreateOrderRequestDto invalidItemName = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemInvalidName));
        CreateOrderRequestDto invalidItemDescription = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemInvalidDescription));
        CreateOrderRequestDto invalidItemUnityPrice = CreateOrderRequestDtoFixture.buildFromParams(
                "Jhon Doe", "12345678901", "Rua X Bairo X", "12345678", "Cidade X",
                "SP", List.of(itemUnityPriceNull));
        CreateOrderRequestDto invalidItemCount = CreateOrderRequestDtoFixture.buildFromParams(
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orders")
                .content(JsonParserUtil.toJson(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        var response = mockMvc.perform(requestBuilder);

        response.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("errorInputs")
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
    void getOrderById() {
    }

    @Test
    void findByFilter() {
    }

    @Test
    void findOrderHistoryByOrderId() {
    }
}