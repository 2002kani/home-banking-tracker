package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.dto.CategoryDto;
import com.home_banking.transaction_service.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestHeader("X-User-Id") String userId) {
        List<CategoryDto> categories = categoryService.getCategories(UUID.fromString(userId));
        return ResponseEntity.ok(categories);
    }

    @Valid
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CategoryDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request, UUID.fromString(userId)));
    }
}
