package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.MccCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MccCategoryMappingRepository extends JpaRepository<MccCategoryMapping, String> {
}
