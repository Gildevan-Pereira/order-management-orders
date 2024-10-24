package com.ms_order.model.mongodb;


import com.ms_order.model.dto.request.OrderItemDto;
import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_history")
public class OrderHistoryEntity {

    @Id
    private String id;
    private Integer orderId;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private OrderStatusEnum status;
    private String name;
    private String cpf;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private List<ItemHistoryEntity> items;
}
