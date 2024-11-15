package com.ms_order.model.mongodb;


import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_history")
public class OrderHistoryDocument {

    @Id
    private String id;

    private Integer orderId;

    private LocalDateTime createdAt;

    @CreatedDate
    private LocalDateTime historyCreatedAt;

    private BigDecimal amount;
    private OrderStatusEnum status;
    private String name;
    private String cpf;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private LocalDateTime attemptedPaymentAt;
    private List<ItemHistoryDocument> items;
}
