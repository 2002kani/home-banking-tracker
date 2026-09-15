package com.home_banking.account_service.controller;

import com.home_banking.account_service.service.AccountService;
import com.home_banking.account_service.service.UserAccountService;
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
public class UserAccountController {
    private final UserAccountService userAccountService;

    @DeleteMapping("/{userId}/data")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId) {
        userAccountService.deleteUserAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
