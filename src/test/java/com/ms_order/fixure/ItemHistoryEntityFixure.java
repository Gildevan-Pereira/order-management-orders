package com.ms_order.fixure;

import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import org.modelmapper.ModelMapper;

import java.util.List;

public class ItemHistoryEntityFixure {

    public static List<ItemHistoryDocument> buildDefault(List<ItemEntity> list){
        ModelMapper modelMapper = new ModelMapper();
        return list.stream()
                .map(item -> modelMapper.map(item, ItemHistoryDocument.class))
                .toList();
    }
}
