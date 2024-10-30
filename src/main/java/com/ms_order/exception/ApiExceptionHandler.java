package com.ms_order.exception;

import com.ms_order.exception.dto.ErrorDto;
import com.ms_order.exception.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex) {
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getCode(), ex.getMessage())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }

    @ExceptionHandler(InternalException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDto> handleInternalException(InternalException ex) {
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getCode(), ex.getMessage())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }
}
