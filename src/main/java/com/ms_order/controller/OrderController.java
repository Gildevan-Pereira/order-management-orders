package com.ms_order.controller;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.OrderResponseDto;
import com.ms_order.model.dto.response.OrderHistoryResponseDto;
import com.ms_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid CreateOrderRequestDto dto) {
        log.info("OrderController.createOrder - Request received | requestDto: {}", dto);
        var response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer id) {
        log.info("OrderController.getOrderById - Request received | id: {}", id);
        var response = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> findByFilter(@Valid OrderSearchFilterDto filterDto, Pageable pageable) {
        log.info("OrderController.findByFilters - Request received | requestDto: {}", filterDto);
        return ResponseEntity.ok(orderService.findByFilters(filterDto, pageable));
    }

    @GetMapping("history/{id}")
    public ResponseEntity<List<OrderHistoryResponseDto>> findOrderHistoryByOrderId(@PathVariable Integer id) {
        log.info("OrderController.getOrderHistoryById - Request received | id: {}", id);
        var response = orderService.findOrderHistoryByOrderId(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
