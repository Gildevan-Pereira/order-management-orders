package com.ms_order.model.dto.validations;

import com.ms_order.exception.BusinessException;
import com.ms_order.messages.MessageEnum;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderSearchFilterDtoValidator implements ConstraintValidator<OrderSearchFilterDtoValidation, OrderSearchFilterDto> {

    @Override
    public boolean isValid(OrderSearchFilterDto filterDto, ConstraintValidatorContext context) {

        List<String> errors = new ArrayList<>();

        if ((Objects.nonNull(filterDto.getStartDate()) && Objects.isNull(filterDto.getEndDate())) ||
                (Objects.isNull(filterDto.getStartDate()) && Objects.nonNull(filterDto.getEndDate()))) {
            errors.add(MessageEnum.RANGE_DATE_ERROR.joinCodeAndMessage());
        }
        if (Objects.nonNull(filterDto.getStartDate()) && Objects.nonNull(filterDto.getEndDate()) &&
                filterDto.getStartDate().isAfter(filterDto.getEndDate())) {
            errors.add(MessageEnum.RANGE_DATE_INVALID.joinCodeAndMessage());
        }

        if ((Objects.nonNull(filterDto.getMinAmount()) && Objects.isNull(filterDto.getMaxAmount())) ||
                (Objects.isNull(filterDto.getMinAmount()) && Objects.nonNull(filterDto.getMaxAmount()))) {
            errors.add(MessageEnum.RANGE_AMOUNT_ERROR.joinCodeAndMessage());
        }
        if (Objects.nonNull(filterDto.getMinAmount()) && Objects.nonNull(filterDto.getMaxAmount()) &&
                filterDto.getMinAmount().compareTo(filterDto.getMaxAmount()) > 0) {
            errors.add(MessageEnum.RANGE_AMOUNT_INVALID.joinCodeAndMessage());
        }
        if (!CollectionUtils.isEmpty(filterDto.getStatus())) {
            try {
                for (String status : filterDto.getStatus()) {
                    OrderStatusEnum.fromName(status);
                }
            } catch (BusinessException e) {
                errors.add(e.getCode() + "#" + e.getMessage());
            }
        }

        if (Objects.nonNull(filterDto.getName()) && !filterDto.getName().matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{2,255}$")) {
            errors.add(MessageEnum.CLIENT_NAME_INVALID.joinCodeAndMessage());
        }
        if (Objects.nonNull(filterDto.getCpf()) && !filterDto.getCpf().matches("^\\d{11}$")) {
            errors.add(MessageEnum.CPF_INVALID.joinCodeAndMessage());
        }
        if (Objects.nonNull(filterDto.getCity()) && !filterDto.getCity().matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{3,50}$")) {
            errors.add(MessageEnum.CITY_INVALID.joinCodeAndMessage());
        }
        if (Objects.nonNull(filterDto.getState()) && !filterDto.getState().matches("[^A-Za-z]{2}$")) {
            errors.add(MessageEnum.STATE_INVALID.joinCodeAndMessage());
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
