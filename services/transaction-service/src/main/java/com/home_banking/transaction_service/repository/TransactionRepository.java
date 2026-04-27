package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    boolean existsByExternalId(String externalId);
    Optional<Transaction> findByUserIdAndId(UUID userId, Long id);
}
