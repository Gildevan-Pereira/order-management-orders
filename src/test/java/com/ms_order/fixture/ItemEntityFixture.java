package com.ms_order.fixture;

import com.ms_order.model.dto.request.CreateOrderRequestDto;
import com.ms_order.model.entity.ItemEntity;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

public class ItemEntityFixture {

    public static ItemEntity buildDefault(BigDecimal unityPrice, Integer count){
        return ItemEntity.builder()
                .name("Item")
                .description("Descrição")
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }

    public static List<ItemEntity> buildFromRequestDto(CreateOrderRequestDto requestDto) {
        ModelMapper modelMapper = new ModelMapper();
        return requestDto.getItems().stream()
                .map(item -> modelMapper.map(item, ItemEntity.class))
                .toList();
    }

}
