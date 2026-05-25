package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> getCategories(Long userId);
    CategoryDto createCategory(CategoryDto request, Long userId);
    void deleteCategory(Long id, Long userId);
}
