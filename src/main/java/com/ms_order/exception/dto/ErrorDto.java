package com.ms_order.exception.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private String code;
    private String message;
}
