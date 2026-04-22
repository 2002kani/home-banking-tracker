package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.event.TransactionEvent;
import com.home_banking.transaction_service.mapper.TransactionMapper;
import com.home_banking.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public void persistTransactions(TransactionEvent event) {
        if(!transactionRepository.existsByExternalId(event.getExternalId())){
            transactionRepository.save(TransactionMapper.mapToEntity(event));
        }
    }
}
