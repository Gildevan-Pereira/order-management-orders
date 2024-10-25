package com.ms_order.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
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
    @JsonIgnoreProperties("order")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    private String name;

    private String description;

    private BigDecimal unityPrice;

    private Integer count;

}
