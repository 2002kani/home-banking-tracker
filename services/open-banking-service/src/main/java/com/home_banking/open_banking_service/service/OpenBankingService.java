package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AuthorizeSessionResponse;
import com.home_banking.open_banking_service.dto.StartAuthResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.entity.PendingSession;
import com.home_banking.open_banking_service.event.AccountUpdateEvent;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import com.home_banking.open_banking_service.repository.PendingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class OpenBankingService implements IOpenBankingService {
    private final EnablebankingClient enablebankingClient;
    private final BankSessionRepository bankSessionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PendingSessionRepository pendingSessionRepository;
    private final KafkaPublisherService kafkaPublisherService;
    private final NgrokService ngrokService;

    public OpenBankingService(EnablebankingClient enablebankingClient,
                              BankSessionRepository bankSessionRepository,
                              BankAccountRepository bankAccountRepository,
                              PendingSessionRepository pendingSessionRepository,
                              KafkaPublisherService kafkaPublisherService,
                              NgrokService ngrokService) {
        this.enablebankingClient = enablebankingClient;
        this.bankSessionRepository = bankSessionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.pendingSessionRepository = pendingSessionRepository;
        this.kafkaPublisherService = kafkaPublisherService;
        this.ngrokService = ngrokService;
    }

    @Override
    public StartAuthResponse startAuth(String bank, String country, Long userId) {
        try {
            ngrokService.openTunnel();
        } catch (Exception e) {
            throw new RuntimeException("Failed to open ngrok tunnel", e);
        }
        String state = UUID.randomUUID().toString();
        pendingSessionRepository.save(PendingSession.builder()
                .state(state)
                .userId(userId)
                .createdAt(Instant.now())
                .build());
        return enablebankingClient.startAuthorization(bank, country, state);
    }

    @Override
    public void authAndSave(String code, String state, String bankName, String bankCountry) {
        PendingSession pending = pendingSessionRepository.findById(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        Long userId = pending.getUserId();
        pendingSessionRepository.delete(pending);

        AuthorizeSessionResponse resp = enablebankingClient.authorizeSession(code);

        BankSession session = BankSession.builder()
                .sessionId(resp.getSessionId())
                .userId(userId)
                .status("ACTIVE")
                .bankName(bankName)
                .bankCountry(bankCountry)
                .validUntil(Instant.now().plus(90, ChronoUnit.DAYS))
                .createdAt(Instant.now())
                .build();
        bankSessionRepository.save(session);

        if (resp.getAccounts() != null) {
            resp.getAccounts().forEach(account -> {
                BankAccount bankAccount = BankAccount.builder()
                        .sessionId(resp.getSessionId())
                        .accountUid(account.getUid())
                        .userId(userId)
                        .iban(account.getAccountId().getIban())
                        .currency(account.getCurrency())
                        .name(account.getName())
                        .identificationHash(account.getIdentificationHash())
                        .build();
                bankAccountRepository.save(bankAccount);

                AccountUpdateEvent event = AccountUpdateEvent.builder()
                        .sessionId(session.getSessionId())
                        .accountUid(account.getUid())
                        .userId(userId)
                        .iban(bankAccount.getIban())
                        .currency(account.getCurrency())
                        .balance(getEventBalance(account.getUid()))
                        .name(account.getName())
                        .build();

                kafkaPublisherService.publishBalanceEvent(event);
            });
        }

        ngrokService.closeTunnelDelayed();
    }

    private String getEventBalance(String accountUid) {
        BalancesResponse resp = enablebankingClient.getBalances(accountUid);
        if (resp == null || resp.getBalances() == null) {
            return null;
        }
        return resp.getBalances().getFirst().getBalanceAmount().getAmount();
    }
}
