package com.home_banking.account_service.client;

import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenBankingClient {
    private final WebClient webClient;

    public BanksListResponse getBanks(String country){
        return webClient.get()
                .uri(uri -> uri
                        .path("/aspsps")
                        .queryParamIfPresent("country", Optional.ofNullable(country))
                        .build())
                .retrieve()
                .bodyToMono(BanksListResponse.class)
                .block();
    }

    public StartAuthResponse startAuth(String bank, String country){
        return webClient.post()
                .uri(uri -> uri
                        .path("/auth")
                        .queryParam("bank", bank)
                        .queryParam("country", country)
                        .build())
                .retrieve()
                .bodyToMono(StartAuthResponse.class)
                .block();
    }
}
