package com.home_banking.open_banking_service.utils;

import com.home_banking.open_banking_service.dto.sessionResponses.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdBuilder {

    public String buildTransactionId(TransactionDto tx) {
        String raw = tx.getBookingDate() + "_" +
                tx.getTransactionAmount().getAmount() + "_" +
                tx.getTransactionAmount().getCurrency() + "_" +
                tx.getType().name();
        return Integer.toHexString(raw.hashCode());
    }
}