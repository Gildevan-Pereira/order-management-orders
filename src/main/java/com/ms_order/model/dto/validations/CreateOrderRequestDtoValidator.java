package com.ms_order.model.dto.validations;

import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.CreateOrderRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrderRequestDtoValidator implements ConstraintValidator<CreateOrderRequestDtoValidation, CreateOrderRequestDto> {

    @Override
    public boolean isValid(CreateOrderRequestDto requestDto, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();

//        TODO: Criar constante para REGEX

        if (Objects.isNull(requestDto.getName()) || !requestDto.getName().matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{2,255}$")) {
            errors.add(MessageEnum.CLIENT_NAME_INVALID.joinCodeAndMessage());
        }
        if (Objects.isNull(requestDto.getCpf()) || !requestDto.getCpf().matches("^\\d{11}$")) {
            errors.add(MessageEnum.CPF_INVALID.joinCodeAndMessage());
        }
        if (Objects.isNull(requestDto.getCity()) || !requestDto.getCity().matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{3,50}$")) {
            errors.add(MessageEnum.CITY_INVALID.joinCodeAndMessage());
        }
        if (Objects.isNull(requestDto.getAddress()) || !requestDto.getAddress().matches("^[A-Za-z0-9À-ÖØ-öø-ÿ.,'\\s]{10,255}$")) {
            errors.add(MessageEnum.ADDRESS_INVALID.joinCodeAndMessage());
        }
        if (Objects.isNull(requestDto.getPostalCode()) || !requestDto.getPostalCode().matches("^\\d{8}$")) {
            errors.add(MessageEnum.POSTAL_CODE_INVALID.joinCodeAndMessage());
        }
        if (Objects.isNull(requestDto.getState()) || !requestDto.getState().matches("[^A-Za-z]{2}$")) {
            errors.add(MessageEnum.STATE_INVALID.joinCodeAndMessage());
        }
        if (CollectionUtils.isEmpty(requestDto.getItems())) {
            errors.add(MessageEnum.ITEMS_NOT_EMPTY.joinCodeAndMessage());
        }

        if (!errors.isEmpty()) {
            setErrorMessages(context, errors);
            return false;
        }
        return true;
    }

    private void setErrorMessages(ConstraintValidatorContext context, List<String> errors) {
        context.disableDefaultConstraintViolation();
        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
    }
}
