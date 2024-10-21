package com.ms_order.controller;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto dto) {
        var response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
