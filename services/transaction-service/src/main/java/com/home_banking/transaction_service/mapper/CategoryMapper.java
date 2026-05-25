package com.home_banking.transaction_service.mapper;

import com.home_banking.transaction_service.dto.CategoryDto;
import com.home_banking.transaction_service.entity.Category;

public class CategoryMapper {

    public static Category mapToEntity(CategoryDto categoryDto, Long userId) {
        return Category.builder()
                .userId(userId)
                .name(categoryDto.getName())
                .color(categoryDto.getColor())
                .isSystem(false)
                .build();
    }

    public static CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .color(category.getColor())
                .isSystem(category.getIsSystem())
                .build();
    }
}
