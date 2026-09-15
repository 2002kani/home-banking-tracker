package com.home_banking.open_banking_service.controller;

import com.home_banking.open_banking_service.service.OpenBankingService;
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
public class UserBankingController {
    private final OpenBankingService openBankingService;

    @DeleteMapping("/{userId}/data")
    public ResponseEntity<Void> deleteUserBankingDetails(@PathVariable Long userId) {
        openBankingService.deleteUserBankingDetails(userId);
        return ResponseEntity.noContent().build();
    }
}
