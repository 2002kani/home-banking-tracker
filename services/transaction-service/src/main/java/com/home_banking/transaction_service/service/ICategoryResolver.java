package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CategorySource;

import java.util.Optional;

public interface ICategoryResolver {
    int priority();
    CategorySource source();
    Optional<Category> resolve(Transaction tx, Long userId);
}
