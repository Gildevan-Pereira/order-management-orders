package com.ms_order.specification;

import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<OrderEntity> filterTo(LocalDateTime createdAt, BigDecimal amount,
                                                      OrderStatusEnum status, String name,
                                                      String cpf, String city, String state) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (createdAt != null) {
                predicates.add(criteriaBuilder.equal(root.get("created_at"), createdAt));
            }
            if (amount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount));
            }
            if (amount != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), amount));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (cpf != null && !cpf.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), cpf));
            }
            if (city != null && !city.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("city"), "%" + city + "%"));
            }
            if (state != null && !state.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("state"), state));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
