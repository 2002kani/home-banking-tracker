package com.home_banking.account_service.service;

import com.home_banking.account_service.repository.AccountRepository;
import com.home_banking.account_service.repository.AccountSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final AccountRepository accountRepository;
    private final AccountSnapshotRepository accountSnapshotRepository;

    @Transactional
    public void deleteUserAccount(Long userId){
        accountRepository.deleteAllByUserId(userId);
        accountSnapshotRepository.deleteAllByUserId(userId);
    }
}
