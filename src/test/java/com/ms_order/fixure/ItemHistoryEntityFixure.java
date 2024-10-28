package com.ms_order.fixure;

import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.mongodb.ItemHistoryEntity;
import org.modelmapper.ModelMapper;

import java.util.List;

public class ItemHistoryEntityFixure {

    public static List<ItemHistoryEntity> buildDefault(List<ItemEntity> list){
        ModelMapper modelMapper = new ModelMapper();
        return list.stream()
                .map(item -> modelMapper.map(item, ItemHistoryEntity.class))
                .toList();
    }
}
