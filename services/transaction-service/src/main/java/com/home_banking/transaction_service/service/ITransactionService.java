package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.event.TransactionEvent;

import java.util.List;

public interface ITransactionService {
    void persistTransactions(TransactionEvent event);
}
