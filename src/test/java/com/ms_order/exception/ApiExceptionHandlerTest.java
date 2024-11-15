package com.ms_order.exception;

import com.ms_order.exception.dto.ErrorResponseDto;
import com.ms_order.messages.MessageEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    private ApiExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp(){
        exceptionHandler = new ApiExceptionHandler();
    }

    @Test
    void handleException() {
        Exception exception = new Exception("Error");

        var response = exceptionHandler.handleException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ErrorResponseDto.class);
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().getFirst().getCode()).isEqualTo(MessageEnum.GENERIC_ERROR.getCode());
        assertThat(response.getBody().getErrors().getFirst().getMessage()).isEqualTo(MessageEnum.GENERIC_ERROR.getMessage());
    }

    @Test
    void handleBusinessException() {
        BusinessException businessException = new BusinessException(MessageEnum.ORDER_NOT_FOUND, "1", HttpStatus.BAD_REQUEST);

        var response = exceptionHandler.handleBusinessException(businessException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ErrorResponseDto.class);
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().getFirst().getCode())
                .isEqualTo(MessageEnum.ORDER_NOT_FOUND.getCode());
        assertThat(response.getBody().getErrors().getFirst().getMessage())
                .isEqualTo(String.format(MessageEnum.ORDER_NOT_FOUND.getMessage(), 1));
    }

    @Test
    void handleBusinessExceptionWhenMessageParamIsNull() {
        BusinessException businessException = new BusinessException(MessageEnum.ORDER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);

        var response = exceptionHandler.handleBusinessException(businessException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ErrorResponseDto.class);
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().getFirst().getCode())
                .isEqualTo(MessageEnum.ORDER_NOT_FOUND.getCode());
        assertThat(response.getBody().getErrors().getFirst().getMessage())
                .isEqualTo(MessageEnum.ORDER_NOT_FOUND.getMessage());
    }

    @Test
    void handleInternalException() {
        InternalException internalException = new InternalException(MessageEnum.JSON_PARSE_ERROR, HttpStatus.BAD_GATEWAY);

        var response = exceptionHandler.handleInternalException(internalException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ErrorResponseDto.class);
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().getFirst().getCode())
                .isEqualTo(MessageEnum.JSON_PARSE_ERROR.getCode());
        assertThat(response.getBody().getErrors().getFirst().getMessage())
                .isEqualTo(MessageEnum.JSON_PARSE_ERROR.getMessage());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException notValidException = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        var objectError = new ObjectError("ObjectName", "01#Error");
        var fieldError = new FieldError("ObjectName", "fieldName", "02#Error");

        List<ObjectError> errors = List.of(objectError, fieldError);

        when(notValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        var response = exceptionHandler.handleMethodArgumentNotValidException(notValidException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ErrorResponseDto.class);
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().get(0).getCode()).isEqualTo("01");
        assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("Error");
        assertThat(response.getBody().getErrors().get(1).getCode()).isEqualTo("02");
        assertThat(response.getBody().getErrors().get(1).getMessage()).isEqualTo("Error");
    }
}