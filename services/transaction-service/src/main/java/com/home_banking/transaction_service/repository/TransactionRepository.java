package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByExternalId(String externalId);
    List<Transaction> findByBookingDateBetweenAndUserId(LocalDate from, LocalDate to, UUID userId);
    List<Transaction> findAllByUserId(UUID userId);

}
