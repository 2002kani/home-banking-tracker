package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.event.TransactionEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    List<TransactionDto> getTransactions(UUID userId, LocalDate from, LocalDate to, CreditDebitIndicator type);
    void persistTransactions(TransactionEvent event);
    TransactionDto getTransactionById(Long id);
    void categorizeTransaction(UUID userId, Long id, Long categoryId);
}
