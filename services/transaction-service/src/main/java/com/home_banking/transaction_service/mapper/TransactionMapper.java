package com.home_banking.transaction_service.mapper;

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
                .creditorName(event.getCreditor().getName())
                .debtorName(event.getDebtor().getName())
                .type(event.getType())
                .bookingDate(event.getBookingDate())
                .status(event.getStatus())
                .createdAt(Instant.now())
                .build();
    }
}
