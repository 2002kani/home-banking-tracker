package com.home_banking.account_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${open-banking.api.base-url}")
    public String baseUrl;

    @Bean
    public WebClient openBankingWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
