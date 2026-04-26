package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    boolean existsByExternalId(String externalId);
    Optional<Transaction> findByUserIdAndId(UUID userId, Long id);
}
