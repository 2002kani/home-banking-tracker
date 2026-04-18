package com.home_banking.open_banking_service.controller;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AspspsListResponse;
import com.home_banking.open_banking_service.dto.StartAuthResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.TransactionsResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.service.OpenBankingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/home-banking")
@AllArgsConstructor
public class OpenBankingController {
    private final EnablebankingClient enablebankingClient;
    private final OpenBankingService openBankingService;
    private final BankAccountRepository  bankAccountRepository;

    /*
    * Interacts with the HTTP Call from Account Service
    */
    @GetMapping("/aspsps")
    public AspspsListResponse getAspsps(@RequestParam(required = false) String country){
        return enablebankingClient.getAvailableBanks(country);
    }

    @PostMapping("/auth")
    public ResponseEntity<StartAuthResponse> startAuthorization(@RequestParam String bank, @RequestParam String country){
        StartAuthResponse resp = enablebankingClient.startAuthorization(bank, country);
        return ResponseEntity.ok(resp);
    }

    /*
    * After valid authorization, enable Banking redirects you to this callback url.
    * Exchange the code with sessionId for api calls to enableBanking
    */
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription,
            @RequestHeader(value = "X-User-Id", required = false) String userId)
    {
        if(error != null){
            return ResponseEntity.badRequest().body(errorDescription);
        }
        UUID userUuid = userId != null ? UUID.fromString(userId) : UUID.fromString("00000000-0000-0000-0000-000000000001");

        // TODO: exchange hardcoded logic to more substantial solution
        String sessionId = openBankingService.authAndSave(code, state, "Sparkasse", "DE", userUuid);
        return ResponseEntity.ok(sessionId);
    }

    /*
    * The following two endpoints are for postman test purposes only!
    */
    @GetMapping("/balances")
    public ResponseEntity<BalancesResponse> getBalances(){
        BankAccount bankAccount = bankAccountRepository.findAll().get(0);  // Workaround bis production
        BalancesResponse resp = enablebankingClient.getBalances(bankAccount.getAccountUid());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getTransactions(){
        BankAccount bankAccount = bankAccountRepository.findAll().get(0);
        TransactionsResponse resp = enablebankingClient.getAllTransactions(bankAccount.getAccountUid());
        return ResponseEntity.ok(resp);
    }
}
