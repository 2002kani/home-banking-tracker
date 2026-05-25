package com.home_banking.open_banking_service.controller;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AspspsListResponse;
import com.home_banking.open_banking_service.dto.StartAuthResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.TransactionsResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.service.IOpenBankingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/open-banking")
@AllArgsConstructor
@Slf4j
public class OpenBankingController {
    private final EnablebankingClient enablebankingClient;
    private final IOpenBankingService openBankingService;
    private final BankAccountRepository bankAccountRepository;

    @GetMapping("/aspsps")
    public AspspsListResponse getAspsps(@RequestParam(required = false) String country) {
        return enablebankingClient.getAvailableBanks(country);
    }

    /*
     * Called by the logged-in user through the gateway → X-User-Id is available.
     * Generates a random state, stores state→userId mapping, then redirects to EnableBanking.
     */
    @PostMapping("/auth")
    public ResponseEntity<StartAuthResponse> startAuthorization(
            @RequestParam String bank,
            @RequestParam String country,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(openBankingService.startAuth(bank, country, userId));
    }

    /*
     * Called by EnableBanking after the user authorizes (external redirect, no gateway).
     * userId is resolved from the state parameter that was stored during /auth.
     */
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription) {
        if (error != null) {
            return ResponseEntity.badRequest().body(errorDescription);
        }
        // TODO: exchange hardcoded bank/country with dynamic solution
        String sessionId = openBankingService.authAndSave(code, state, "Sparkasse", "DE");
        return ResponseEntity.ok(sessionId);
    }

    /*
     * The following two endpoints are for postman test purposes only!
     */
    @GetMapping("/balances")
    public ResponseEntity<BalancesResponse> getBalances() {
        BankAccount bankAccount = bankAccountRepository.findAll().get(0);
        return ResponseEntity.ok(enablebankingClient.getBalances(bankAccount.getAccountUid()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getTransactions() {
        BankAccount bankAccount = bankAccountRepository.findAll().get(0);
        return ResponseEntity.ok(enablebankingClient.getAllTransactions(bankAccount.getAccountUid()));
    }
}
