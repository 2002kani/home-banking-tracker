package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public boolean existsByExternalId(String externalId);
}
