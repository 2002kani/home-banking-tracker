package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    @Override
    public void updateTransactions(TransactionEvent event) {

    }
}
