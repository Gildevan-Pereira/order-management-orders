package com.ms_order.controller;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.dto.response.CreateOrderResponseDto;
import com.ms_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto dto) {
        log.info("OrderController.createOrder - Request received | requestDto: {}", dto);
        var response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateOrderResponseDto> getOrderById(@PathVariable Integer id) {
        log.info("OrderController.getOrderById - Request received | id: {}", id);
        var response = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CreateOrderResponseDto>> findByFilter(@Valid OrderSearchFilterDto filterDto, Pageable pageable) {
        log.info("OrderController.findByFilters - Request received | requestDto: {}", filterDto);
        return ResponseEntity.ok(orderService.findByFilters(filterDto, pageable));
    }

}
