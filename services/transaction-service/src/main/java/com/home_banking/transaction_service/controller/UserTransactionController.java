package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.service.IUserTransactionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class UserTransactionController {
    private final IUserTransactionService userTransactionService;

    @DeleteMapping("/{userId}/data")
    public ResponseEntity<Void> deleteUserData(@PathVariable Long userId) {
        userTransactionService.deleteUserData(userId);
        return ResponseEntity.noContent().build();
    }
}
