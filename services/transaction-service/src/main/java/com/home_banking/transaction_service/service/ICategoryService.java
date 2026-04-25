package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.CategoryDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    List<CategoryDto> getCategories(UUID userId);
    CategoryDto createCategory(CategoryDto request, UUID userId);
}
