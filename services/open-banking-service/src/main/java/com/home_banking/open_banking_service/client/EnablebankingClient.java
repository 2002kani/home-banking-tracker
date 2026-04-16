package com.home_banking.open_banking_service.client;

import com.home_banking.open_banking_service.dto.*;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.TransactionsResponse;
import com.home_banking.open_banking_service.utils.EnableBankingJwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EnablebankingClient {
    private final WebClient webClient;
    private final EnableBankingJwtProvider jwtProvider;

    @Value("${enablebanking.api.redirect-url}")
    private String redirectUrl;

    public EnablebankingClient(WebClient webClient,  EnableBankingJwtProvider jwtProvider) {
        this.webClient = webClient;
        this.jwtProvider = jwtProvider;
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

    public StartAuthResponse startAuthorization(String bank, String country){
        StartAuthRequest request = StartAuthRequest.builder()
                .access(AccessDto.builder()
                        .validUntil(Instant.now().plus(90, ChronoUnit.DAYS).toString())
                        .build())
                .aspsp(AspspMinDto.builder()
                        .name(bank)
                        .country(country)
                        .build())
                .state(UUID.randomUUID().toString())
                .redirectUrl(redirectUrl)
                .build();

        log.info("send to enable Banking: {}", request);

        return webClient.post()
                .uri("/auth")
                .header("Authorization", "Bearer " + jwtProvider.generateJwt())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StartAuthResponse.class)
                .block();
    }

    public AuthorizeSessionResponse authorizeSession(String code){
        return webClient.post()
                .uri("/sessions")
                .header("Authorization", "Bearer " + jwtProvider.generateJwt())
                .bodyValue(AuthSessionRequest.builder().code(code).build())
                .retrieve()
                .bodyToMono(AuthorizeSessionResponse.class)
                .block();
    }

    public BalancesResponse getBalances(String accountId){
        return webClient.get()
                .uri("/accounts/" + accountId + "/balances")
                .header("Authorization", "Bearer " + jwtProvider.generateJwt())
                .retrieve()
                .bodyToMono(BalancesResponse.class)
                .block();
    }

    public TransactionsResponse getAllTransactions(String accountId){
        return webClient.get()
                .uri("/accounts/" + accountId + "/transactions")
                .header("Authorization", "Bearer " + jwtProvider.generateJwt())
                .retrieve()
                .bodyToMono(TransactionsResponse.class)
                .block();
    }

    // TODO: hier getTransactionsById erstellen
}
