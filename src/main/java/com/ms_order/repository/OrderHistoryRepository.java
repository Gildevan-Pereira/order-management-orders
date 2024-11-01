package com.ms_order.repository;

import com.ms_order.model.mongodb.OrderHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderHistoryRepository extends MongoRepository<OrderHistoryDocument, String> {
    List<OrderHistoryDocument> findByOrderId(Integer id);
}
