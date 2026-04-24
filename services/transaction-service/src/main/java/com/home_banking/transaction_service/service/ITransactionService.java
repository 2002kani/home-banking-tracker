package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.event.TransactionEvent;

import java.time.LocalDate;
import java.util.List;

public interface ITransactionService {
    List<TransactionDto> getTransactions();
    void persistTransactions(TransactionEvent event);
    TransactionDto getTransactionById(Long id);
    List<TransactionDto> getTransactionsByDate(LocalDate dateFrom, LocalDate dateTo);
}
