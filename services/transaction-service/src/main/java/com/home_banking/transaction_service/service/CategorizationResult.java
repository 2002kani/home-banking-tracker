package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.enums.CategorySource;

public record CategorizationResult(Category category, CategorySource categorySource) {

    public static CategorizationResult none() {
        return new CategorizationResult(null, CategorySource.NONE);
    }

    public boolean matched(){
        return category != null;
    }
}
