package com.home_banking.transaction_service.mapper;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.event.TransactionEvent;

import java.time.Instant;

public class TransactionMapper {
    public static Transaction mapToEntity(TransactionEvent event) {
        return Transaction.builder()
                .sessionId(event.getSessionId())
                .accountId(event.getAccountId())
                .externalId(event.getExternalId())
                .userId(event.getUserId())
                .currency(event.getCurrency())
                .amount(event.getAmount())
                .creditorName(event.getCreditor() != null ? event.getCreditor().getName() : null)
                .debtorName(event.getDebtor() != null ? event.getDebtor().getName() : null)
                .type(event.getType())
                .bookingDate(event.getBookingDate())
                .status(event.getStatus())
                .createdAt(Instant.now())
                .build();
    }

    public static TransactionDto mapToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .creditorName(transaction.getCreditorName())
                .debtorName(transaction.getDebtorName())
                .type(transaction.getType())
                .bookingDate(transaction.getBookingDate())
                .status(transaction.getStatus())
                .categoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null)
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : null)
                .build();
    }
}
