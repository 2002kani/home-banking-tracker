package com.home_banking.open_banking_service.controller;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AspspsListResponse;
import com.home_banking.open_banking_service.dto.StartAuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/home-banking")
@AllArgsConstructor
public class OpenBankingController {
    private final EnablebankingClient enablebankingClient;

    /*
    * Interacts with the HTTP Call from Account Service
    */
    @GetMapping("/aspsps")
    public AspspsListResponse getAspsps(@RequestParam String country){
        return enablebankingClient.getAvailableBanks(country);
    }

    @PostMapping("/auth")
    public ResponseEntity<StartAuthResponse> startAuthorization(@RequestParam String bank, @RequestParam String country){
        StartAuthResponse resp = enablebankingClient.startAuthorization(bank, country);
        return ResponseEntity.ok(resp);
    }
}
