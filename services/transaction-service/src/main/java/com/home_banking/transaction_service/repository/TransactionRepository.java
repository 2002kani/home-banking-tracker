package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public boolean existsByExternalId(String externalId);
    public List<Transaction> findByBookingDateBetween(LocalDate from, LocalDate to);
}
