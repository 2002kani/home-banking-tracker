package com.home_banking.open_banking_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class EnablebankingClient {
    private final WebClient webClient;


    public EnablebankingClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
