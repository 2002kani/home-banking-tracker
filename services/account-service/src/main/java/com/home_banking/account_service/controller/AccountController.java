package com.home_banking.account_service.controller;

import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/banks")
    public ResponseEntity<BanksListResponse> getAvailableBanks(@RequestParam(required = false) String country){
        return ResponseEntity.ok(accountService.getAvailableBanks(country));
    }

    // Initiate auth-flow on open-banking-service
    @PostMapping("/auth")
    public ResponseEntity<StartAuthResponse> startAuthorization(
            @RequestParam String bank,
            @RequestParam String country,
            @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(accountService.startAuth(bank, country, userId)); // TODO: Besser ggf autom. weiterleiten in der ui (?)
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getAccount(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ){
        AccountDto account = accountService.getAccount(id, userId);
        return ResponseEntity.ok(account);
    }
}
