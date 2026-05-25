package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.CategoryDto;
import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.mapper.CategoryMapper;
import com.home_banking.transaction_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Long userId) {
         return categoryRepository.findAllByUserIdOrIsSystemTrue(userId)
                 .stream()
                 .map(CategoryMapper::mapToDto)
                 .toList();
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto request, Long userId) {
        Category category = CategoryMapper.mapToEntity(request, userId);
        categoryRepository.save(category);
        return CategoryMapper.mapToDto(category);
    }

    @Override
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findByIdForUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if(category.getIsSystem() == true){
            throw new RuntimeException("System-Kategorien können nicht gelöscht werden");
        }

        categoryRepository.delete(category);
    }
}
