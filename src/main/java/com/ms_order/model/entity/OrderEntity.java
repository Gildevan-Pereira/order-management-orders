package com.ms_order.model.entity;

import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "order_table")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "client_name")
    private String name;

    @Column(name = "client_cpf")
    private String cpf;

    @Column(name = "client_address")
    private String address;

    @Column(name = "client_postal_code")
    private String postalCode;

    @Column(name = "client_city")
    private String city;

    @Column(name = "client_state")
    private String state;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<ItemEntity> items;
}
