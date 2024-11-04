package com.ms_order.fixture;

import com.ms_order.model.entity.ItemEntity;
import com.ms_order.model.mongodb.ItemHistoryDocument;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

public class ItemHistoryDocumentFixture {

    public static ItemHistoryDocument buildDefault(BigDecimal unityPrice, Integer count){
        return ItemHistoryDocument.builder()
                .name("Item")
                .description("Descrição")
                .unityPrice(unityPrice)
                .count(count)
                .build();
    }

    public static List<ItemHistoryDocument> buildFromEntity(List<ItemEntity> list){
        ModelMapper modelMapper = new ModelMapper();
        return list.stream()
                .map(item -> modelMapper.map(item, ItemHistoryDocument.class))
                .toList();
    }

}
