package com.ms_order.specification;

import com.ms_order.fixture.OrderSearchFilterDtoFixture;
import com.ms_order.model.dto.request.OrderSearchFilterDto;
import com.ms_order.model.entity.OrderEntity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderSpecificationTest {

    @Mock
    private Root<OrderEntity> root;
    @Mock
    private CriteriaQuery<OrderEntity> criteriaQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Predicate predicate;
    @Mock
    private Path path;

    @Test
    void shouldCreateSpecificationForAllFilters() {
        var filters = OrderSearchFilterDtoFixture.buildDefault();
        var endDateTime = filters.getEndDate().atTime(23, 59, 59);
        when(root.get("id")).thenReturn(path);
        when(path.in(filters.getIds())).thenReturn(predicate);

        when(root.get("createdAt")).thenReturn(path);
        when(criteriaBuilder.greaterThanOrEqualTo(path, filters.getStartDate())).thenReturn(predicate);
        when(criteriaBuilder.lessThanOrEqualTo(path, endDateTime)).thenReturn(predicate);

        when(root.get("amount")).thenReturn(path);
        when(criteriaBuilder.greaterThanOrEqualTo(path, filters.getMinAmount())).thenReturn(predicate);
        when(criteriaBuilder.lessThanOrEqualTo(path, filters.getMaxAmount())).thenReturn(predicate);

        when(root.get("status")).thenReturn(path);
        when(path.in(filters.getStatus())).thenReturn(predicate);

        when(root.get("name")).thenReturn(path);
        when(criteriaBuilder.like(path, "%" + filters.getName() + "%")).thenReturn(predicate);

        when(root.get("cpf")).thenReturn(path);
        when(criteriaBuilder.equal(path, filters.getCpf())).thenReturn(predicate);

        when(root.get("city")).thenReturn(path);
        when(criteriaBuilder.like(path, "%" + filters.getCity() + "%")).thenReturn(predicate);

        when(root.get("state")).thenReturn(path);
        when(criteriaBuilder.equal(path, filters.getState())).thenReturn(predicate);

        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<OrderEntity> specification = OrderSpecification.filterTo(filters);
        Predicate generatedPredicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertThat(generatedPredicate).isEqualTo(predicate);

        verify(path).in(filters.getIds());
        verify(criteriaBuilder).greaterThanOrEqualTo(path, filters.getStartDate());
        verify(criteriaBuilder).lessThanOrEqualTo(path, endDateTime);
        verify(criteriaBuilder).greaterThanOrEqualTo(path, filters.getMinAmount());
        verify(criteriaBuilder).lessThanOrEqualTo(path, filters.getMaxAmount());
        verify(path).in(filters.getStatus());
        verify(criteriaBuilder).like(path, "%" + filters.getName() + "%");
        verify(criteriaBuilder).equal(path, filters.getCpf());
        verify(criteriaBuilder).like(path, "%" + filters.getCity() + "%");
        verify(criteriaBuilder).equal(path, filters.getState());

    }

    @Test
    void shouldCreateEmptySpecificationWhenAllFiltersIsNull() {
        var filters = OrderSearchFilterDto.builder().build();

        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<OrderEntity> specification = OrderSpecification.filterTo(filters);
        Predicate generatedPredicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertThat(generatedPredicate).isEqualTo(predicate);

        verify(criteriaBuilder).and(any(Predicate[].class));
        verifyNoMoreInteractions(root, criteriaBuilder);
    }

    @Test
    void shouldCreateEmptySpecificationWhenAllFiltersIsEmpty() {
        var filters = OrderSearchFilterDtoFixture.buildEmpty();

        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<OrderEntity> specification = OrderSpecification.filterTo(filters);
        Predicate generatedPredicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertThat(generatedPredicate).isEqualTo(predicate);

        verify(criteriaBuilder).and(any(Predicate[].class));
        verifyNoMoreInteractions(root, criteriaBuilder);
    }
}