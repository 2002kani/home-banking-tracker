package com.home_banking.open_banking_service.controller;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AspspsListResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
