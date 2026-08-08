package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.constants.CategoryPriorities;
import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.entity.MccCategoryMapping;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CategorySource;
import com.home_banking.transaction_service.repository.MccCategoryMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MccResolver implements ICategoryResolver{
    private final MccCategoryMappingRepository mccCategoryMappingRepository;

    @Override
    public int priority() {
        return CategoryPriorities.MCC;
    }

    @Override
    public CategorySource source() {
        return CategorySource.MCC;
    }

    @Override
    public Optional<Category> resolve(Transaction tx, Long userId) {
        if(tx.getMerchantCategoryCode() == null){
            return Optional.empty(); // for example if no online banking, or mcc just not set
        }
        return mccCategoryMappingRepository.findById(tx.getMerchantCategoryCode())
                .map(MccCategoryMapping::getCategory);
    }
}
