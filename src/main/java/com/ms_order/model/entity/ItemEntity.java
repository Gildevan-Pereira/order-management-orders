package com.ms_order.model.entity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "ITEM")
@SuperBuilder
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderEntity orderId;

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;

}
