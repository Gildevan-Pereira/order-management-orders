package com.ms_order.model.dto.request;

import com.ms_order.model.dto.validations.OrderSearchFilterDtoValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@OrderSearchFilterDtoValidation
public class OrderSearchFilterDto {

    private List<Integer> ids;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private List<String> status;
    private String name;
    private String cpf;
    private String city;
    private String state;
}
