package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.event.TransactionEvent;

public interface ITransactionService {
    public void updateTransactions(TransactionEvent event);
}
