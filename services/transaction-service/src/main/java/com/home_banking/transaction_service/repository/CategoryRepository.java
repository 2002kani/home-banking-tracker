package com.home_banking.transaction_service.repository;

import com.home_banking.transaction_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByUserIdOrIsSystemTrue(Long userId);

    @Query("SELECT c FROM Category c WHERE c.id = :id AND (c.userId = :userId OR c.isSystem = true)")
    Optional<Category> findByIdForUser(@Param("id") Long id, @Param("userId") Long userId);
}
