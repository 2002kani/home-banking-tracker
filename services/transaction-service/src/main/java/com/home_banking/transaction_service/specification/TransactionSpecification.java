package com.home_banking.transaction_service.specification;

import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionSpecification {
    public static Specification<Transaction> byUserId(UUID userId) {
        return (root, query, cb) ->
            cb.equal(root.get("userId"), userId);
    }

    public static Specification<Transaction> byType(CreditDebitIndicator type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> byDateBetween(LocalDate dateFrom, LocalDate dateTo) {
        return (root, query, cb) -> {
            if(dateFrom == null && dateTo == null) return null;
            if(dateFrom == null) return cb.lessThanOrEqualTo(root.get("bookingDate"), dateTo);
            if(dateTo == null) return cb.greaterThanOrEqualTo(root.get("bookingDate"), dateFrom);
            return cb.between(root.get("bookingDate"), dateFrom, dateTo);
        };
    }

    public static Specification<Transaction> byCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }
}
