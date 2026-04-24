package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.event.TransactionEvent;
import com.home_banking.transaction_service.mapper.TransactionMapper;
import com.home_banking.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;


    @Override
    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionMapper::mapToDto)
                .toList();
    }

    @Override
    public void persistTransactions(TransactionEvent event) {
        if(!transactionRepository.existsByExternalId(event.getExternalId())){
            transactionRepository.save(TransactionMapper.mapToEntity(event));
        }
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<TransactionDto> getTransactionsByDate(LocalDate dateFrom, LocalDate dateTo) {
        List<Transaction> transactions = transactionRepository.findByBookingDateBetween(dateFrom, dateTo);

        return transactions.stream()
                .map(TransactionMapper::mapToDto)
                .toList();
    }
}
