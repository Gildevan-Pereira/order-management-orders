package com.ms_order.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    private String name;

    private String cpf;

    private String address;

    private String postalCode;

    private String city;

    private String state;

    private List<OrderItemDto> items;
}
