package com.ms_order.specification;

import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.entity.OrderEntity;
import com.ms_order.model.enums.OrderStatusEnum;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<OrderEntity> filterTo(OrderSearchFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

//          TODO: Criar validação para o dto
//          TODO: Adicionar filtro por IDs

            if (filterDto.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filterDto.getStartDate()));
            }
            if (filterDto.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),  filterDto.getEndDate().atTime(23, 59, 59)));
            }
            if (filterDto.getMinAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filterDto.getMinAmount()));
            }
            if (filterDto.getMaxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filterDto.getMaxAmount()));
            }
            if (filterDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), OrderStatusEnum.fromName(filterDto.getStatus())));
            }
            if (StringUtils.hasText(filterDto.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filterDto.getName() + "%"));
            }
            if (StringUtils.hasText(filterDto.getCpf())) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), filterDto.getCpf()));
            }
            if (StringUtils.hasText(filterDto.getCity())) {
                predicates.add(criteriaBuilder.like(root.get("city"), "%" + filterDto.getCity() + "%"));
            }
            if (StringUtils.hasText(filterDto.getState())) {
                predicates.add(criteriaBuilder.equal(root.get("state"), filterDto.getState()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
