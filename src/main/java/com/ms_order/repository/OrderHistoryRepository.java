package com.ms_order.repository;

import com.ms_order.model.mongodb.OrderHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderHistoryRepository extends MongoRepository<OrderHistoryDocument, String> {
}
