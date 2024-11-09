package com.ms_order.model.dto.request;

import com.ms_order.model.dto.validations.CreateOrderRequestDtoValidation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CreateOrderRequestDtoValidation
public class CreateOrderRequestDto {

    private String name;

    private String cpf;

    private String address;

    private String postalCode;

    private String city;

    private String state;

    @Valid
    private List<OrderItemDto> items;

}
