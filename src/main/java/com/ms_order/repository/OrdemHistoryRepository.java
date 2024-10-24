package com.ms_order.repository;

import com.ms_order.model.mongodb.OrderHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrdemHistoryRepository extends MongoRepository<OrderHistoryEntity, String> {
}
