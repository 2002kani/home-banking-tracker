package com.home_banking.auth_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

/*
Ruft die internen Lösch-Endpunkte der anderen Services auf.
Jeder Aufruf ist idempotent, ein Retry nach Teilausfall ist daher unkritisch.
*/
@Service
@Slf4j
public class UserClient {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;
    private final String transactionsUrl;
    private final String accountUrl;
    private final String openBankingUrl;

    public UserClient(
            WebClient webClient,
            @Value("${transaction-service.base-url}") String transactionsUrl,
            @Value("${account-service.base-url}") String accountUrl,
            @Value("${open-banking-service.base-url}") String openBankingUrl
    ) {
        this.webClient = webClient;
        this.transactionsUrl = transactionsUrl;
        this.accountUrl = accountUrl;
        this.openBankingUrl = openBankingUrl;
    }

    public void deleteTransactionData(Long userId) {
        deleteUserData(transactionsUrl, "transaction-service", userId);
        deleteUserData(accountUrl, "account-service", userId);
        deleteUserData(openBankingUrl, "open-banking-service", userId);
    }

    private void deleteUserData(String baseUrl, String serviceName, Long userId) {
        try {
            webClient.delete()
                    .uri(baseUrl + "/internal/users/{userId}/data", userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response
                            .bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new IllegalStateException(
                                    "Status " + response.statusCode() + " " + body)))
                    .toBodilessEntity()
                    .timeout(TIMEOUT)
                    .block();
        } catch (Exception e) {
            log.error("Löschen der Userdaten in {} fehlgeschlagen für userId={}", serviceName, userId, e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Userdaten in " + serviceName + " konnten nicht gelöscht werden");
        }
    }
}
