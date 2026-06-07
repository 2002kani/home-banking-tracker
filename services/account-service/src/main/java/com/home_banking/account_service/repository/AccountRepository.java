package com.home_banking.account_service.repository;

import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.entitiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUserId(Long userId);
    Optional<Account> findByUidAndUserId(String uid, Long userId);
    Optional<Account> findByIdAndUserId(Long id, Long userId);
}
