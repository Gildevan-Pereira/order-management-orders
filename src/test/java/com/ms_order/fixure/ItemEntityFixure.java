package com.ms_order.fixure;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.entity.ItemEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemEntityFixure {

    public static List<ItemEntity> buildDefault(CreateOrderRequestDto requestDto) {
        ModelMapper modelMapper = new ModelMapper();
        return requestDto.getItems().stream()
                .map(item -> modelMapper.map(item, ItemEntity.class))
                .toList();
    }
}
