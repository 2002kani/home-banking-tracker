package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    boolean existsByExternalId(String externalId);

    Optional<Transaction> findByUserIdAndId(Long userId, Long id);

    @Query("SELECT COALESCE(SUM(CAST(t.amount AS big_decimal)), 0) " +
            "FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.type = :type " +
            "AND t.bookingDate BETWEEN :from AND :to")
    BigDecimal sumByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId,
            @Param("type") CreditDebitIndicator type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


}
