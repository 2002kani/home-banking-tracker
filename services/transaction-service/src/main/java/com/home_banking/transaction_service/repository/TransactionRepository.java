package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByExternalId(String externalId);
    List<Transaction> findByBookingDateBetweenAndUserId(LocalDate from, LocalDate to, UUID userId);
    List<Transaction> findAllByUserId(UUID userId);
    Optional<Transaction> findByUserIdAndId(UUID userId, Long id);
    List<Transaction> findByUserIdAndType(UUID userId, CreditDebitIndicator type);
}
