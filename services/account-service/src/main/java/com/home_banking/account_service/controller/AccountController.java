package com.home_banking.account_service.controller;

import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.NetWorthDto;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.service.IAccountService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(accountService.startAuth(bank, country, userId));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(accountService.getAccounts(userId));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getAccount(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ){
        AccountDto account = accountService.getAccount(id, userId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/summary/net-worth")
    public ResponseEntity<NetWorthDto> getAccountNetWorth(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    )
    {
        return ResponseEntity.ok(accountService.getNetWorth(userId));
    }
}
