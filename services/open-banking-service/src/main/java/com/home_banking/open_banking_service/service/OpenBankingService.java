package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AuthorizeSessionResponse;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OpenBankingService implements IOpenBankingService {
    private final EnablebankingClient enablebankingClient;
    private final BankSessionRepository bankSessionRepository;

    public OpenBankingService(EnablebankingClient enablebankingClient,  BankSessionRepository bankSessionRepository) {
        this.enablebankingClient = enablebankingClient;
        this.bankSessionRepository = bankSessionRepository;
    }

    @Override
    public String authAndSave(String code, String state, String bankName, String bankCountry) {
        AuthorizeSessionResponse resp = enablebankingClient.authorizeSession(code);

        BankSession session = BankSession.builder()
                .sessionId(resp.getSessionId())
                .status(state)
                .bankName(bankName)
                .bankCountry(bankCountry)
                .validUntil(Instant.now().plus(90, ChronoUnit.DAYS))
                .createdAt(Instant.now())
                .build();
        
        bankSessionRepository.save(session);
        return resp.getSessionId();
    }
}
