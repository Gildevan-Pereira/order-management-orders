package com.ms_order.model.entity;

import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@SuperBuilder
@Table(name = "ORDER")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private String name;

    private String cpf;

    private String address;

    private String postalCode;

    private String city;

    private String state;
}
