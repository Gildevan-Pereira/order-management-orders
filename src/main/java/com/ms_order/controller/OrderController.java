package com.ms_order.controller;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.model.enums.OrderStatusEnum;
import com.ms_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @GetMapping
    public ResponseEntity<CreateOrderResponseDto> getOrderById(@RequestParam Integer id) {
        var response = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("filter")
    public Page<CreateOrderResponseDto> findByFilter(
        @RequestParam(required = false) LocalDateTime createdAt,
        @RequestParam(required = false) BigDecimal amount,
        @RequestParam(required = false) OrderStatusEnum status,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state,
        Pageable pageable) {

        return orderService.findByFilter(createdAt, amount, status, name, cpf, city, state, pageable);
    }

}
