package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByUserIdOrIsSystemTrue(UUID userId);
    Optional<Category> findByIdAndUserIdOrIsSystemTrue(Long id, UUID userId);
}
