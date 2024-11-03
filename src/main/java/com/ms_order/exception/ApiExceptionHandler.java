package com.ms_order.exception;

import com.ms_order.exception.dto.ErrorDto;
import com.ms_order.exception.dto.ErrorResponseDto;
import com.ms_order.messages.MessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("ApiExceptionHandler.handleException: Error received: | message: {}", ex.getMessage());
        var responseBody = new ErrorResponseDto(
                List.of(new ErrorDto(MessageEnum.GENERIC_ERROR.getCode(),
                        MessageEnum.GENERIC_ERROR.getMessage())));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex) {
        log.error("ApiExceptionHandler.handleBusinessException: Error received: | message: {}", ex.getMessage());
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getCode(), ex.getMessage())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalException(InternalException ex) {
        log.error("ApiExceptionHandler.handleInternalException: Error received: | message: {}", ex.getMessage());
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getCode(), ex.getMessage())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("ApiExceptionHandler.handleMethodArgumentNotValidException: Error received: | message: {}", ex.getMessage());
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<String> errorMessages = errors.stream().map(objectError -> {
           if(objectError instanceof FieldError fieldError){
               return fieldError.getDefaultMessage();
           }
           return objectError.getDefaultMessage();
        }).toList();

        List<ErrorDto> errorDtos = errorMessages.stream().map(error -> {
            var code = error.split("#")[0];
            var message = error.split("#")[1];
            return new ErrorDto(code, message);
        }).toList();

        var responseBody = new ErrorResponseDto(errorDtos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
