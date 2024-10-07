package com.raiffeisen.processor.repository.specification;

import com.raiffeisen.processor.dto.GetPaymentsFilterDto;
import com.raiffeisen.processor.entity.Payment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class PaymentSpecification {
    public static Specification<Payment> getPaymentsByFilter(GetPaymentsFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.getAmount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("amount"), filter.getAmount()));
            }
            if (filter.getCurrency() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), filter.getCurrency()));
            }
            if (filter.getFromAccount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("fromAccount"), filter.getFromAccount()));
            }
            if (filter.getToAccount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("toAccount"), filter.getToAccount()));
            }
            if (filter.getFromTimestamp() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), filter.getFromTimestamp()));
            }
            if (filter.getToTimestamp() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), filter.getToTimestamp()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}