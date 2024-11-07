package com.ms_order.model.dto.validations;

import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.OrderItemDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderItemDtoValidator implements ConstraintValidator<OrderItemDtoValidation, OrderItemDto> {

    @Override
    public boolean isValid(OrderItemDto itemDto, ConstraintValidatorContext context) {

//        TODO: Criar classe abstrata para criar o método setErrorMessages
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(itemDto.getName()) || !itemDto.getName().matches("^[A-Za-z0-9À-ÖØ-öø-ÿ.,'\\s]{5,255}$")) {
            errors.add(MessageEnum.ITEM_NAME_INVALID.joinCodeAndMessage());
        }
        if  (Objects.isNull(itemDto.getDescription()) || !itemDto.getDescription().matches("^[A-Za-z0-9À-ÖØ-öø-ÿ.,'\\s]{5,255}$")) {
            errors.add(MessageEnum.ITEM_DESCRIPTION_INVALID.joinCodeAndMessage());
        }
        if  (Objects.isNull(itemDto.getUnityPrice()) || itemDto.getUnityPrice().scale() > 2 || itemDto.getUnityPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(MessageEnum.ITEM_UNITY_PRICE_INVALID.joinCodeAndMessage());
        }
        if  (Objects.isNull(itemDto.getCount()) || itemDto.getCount() <= 0) {
            errors.add(MessageEnum.ITEM_COUNT_INVALID.joinCodeAndMessage());
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
