package com.home_banking.open_banking_service.repository;

import com.home_banking.open_banking_service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findBySessionId(String sessionId);

    @Modifying
    @Query("DELETE FROM BankAccount s WHERE s.userId = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
