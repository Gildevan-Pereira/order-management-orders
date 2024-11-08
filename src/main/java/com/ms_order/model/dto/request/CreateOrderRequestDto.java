package com.ms_order.model.dto.request;

import com.ms_order.model.dto.validations.CreateOrderRequestDtoValidation;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Data
@Generated("lombok")
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
