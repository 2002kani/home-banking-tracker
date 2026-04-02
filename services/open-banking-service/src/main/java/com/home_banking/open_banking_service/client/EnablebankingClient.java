package com.home_banking.open_banking_service.client;

import com.home_banking.open_banking_service.dto.AspspsListResponse;
import com.home_banking.open_banking_service.utils.EnableBankingJwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@Service
public class EnablebankingClient {
    private final WebClient webClient;
    private final EnableBankingJwtProvider jwtProvider;

    public EnablebankingClient(WebClient webClient) {
        this.webClient = webClient;
        this.jwtProvider = new EnableBankingJwtProvider();
    }

    public AspspsListResponse getAvailableBanks(String country){
        return webClient.get()
                .uri(uri -> uri
                        .path("/aspsps")
                        .queryParamIfPresent("country", Optional.ofNullable(country))
                        .build())
                .header("Authorization", "Bearer " + jwtProvider.generateJwt())
                .retrieve()
                .bodyToMono(AspspsListResponse.class)
                .block();
    }
}
