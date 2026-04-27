package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.event.TransactionEvent;
import com.home_banking.transaction_service.mapper.TransactionMapper;
import com.home_banking.transaction_service.repository.CategoryRepository;
import com.home_banking.transaction_service.repository.TransactionRepository;
import com.home_banking.transaction_service.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<TransactionDto> getTransactions(UUID userId, LocalDate from, LocalDate to, CreditDebitIndicator type,  Long categoryId) {
        Specification<Transaction> specs = Specification
                .where(TransactionSpecification.byUserId(userId))
                .and(TransactionSpecification.byDateBetween(from, to))
                .and(TransactionSpecification.byType(type))
                .and(TransactionSpecification.byCategory(categoryId));

        return transactionRepository.findAll(specs)
                .stream()
                .map(TransactionMapper::mapToDto)
                .toList();
    }

    @Override
    public void persistTransactions(TransactionEvent event) {
        try {
            transactionRepository.save(TransactionMapper.mapToEntity(event));
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate transaction ignored: {}", event.getExternalId());
        }
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public void categorizeTransaction(UUID userId, Long id, Long categoryId) {
        Transaction transaction = transactionRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Category category = categoryRepository.findByIdAndUserIdOrIsSystemTrue(categoryId, userId)
                .orElseThrow(() ->  new RuntimeException("Category not found"));

        transaction.setCategory(category);
        transactionRepository.save(transaction);
    }
}
