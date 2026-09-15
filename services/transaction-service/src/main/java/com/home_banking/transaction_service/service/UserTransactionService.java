package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.repository.CategoryRepository;
import com.home_banking.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTransactionService implements IUserTransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void deleteUserData(Long userId) {
        transactionRepository.deleteAllByUserId(userId);
        categoryRepository.deleteAllByUserId(userId);
    }
}
