package com.ms_order.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "item_table")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderEntity order;

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;

}
