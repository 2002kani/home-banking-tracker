package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.CategoryDto;
import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.mapper.CategoryMapper;
import com.home_banking.transaction_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(UUID userId) {
         return categoryRepository.findAllByUserIdOrIsSystemTrue(userId)
                 .stream()
                 .map(CategoryMapper::mapToDto)
                 .toList();
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto request, UUID userId) {
        Category category = CategoryMapper.mapToEntity(request, userId);
        categoryRepository.save(category);
        return CategoryMapper.mapToDto(category);
    }
}
