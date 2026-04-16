package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.event.TransactionRawEvent;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService implements  ISchedulerService {
    private final BankSessionRepository bankSessionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EnablebankingClient enablebankingClient;
    private final KafkaPublisherService kafkaPublisherService;

    @Override
    @Scheduled(fixedRate = 10000)
    //@Scheduled(cron = "0 0 0 * * *")
    public void syncTransactions() {
        // Get sessions and the bank account for each session
        List<BankSession> activeSessions = bankSessionRepository.findByStatus("ACTIVE");

        activeSessions.forEach(session -> {
            List<BankAccount> accounts = bankAccountRepository.findBySessionId(session.getSessionId());

            accounts.forEach(account -> {
                try{
                    mapAccountTransaction(session, account);
                }catch(Exception e){
                    log.error("Sync failed for Account {}: {}", account.getAccountUid(), e.getMessage());
                }
            });
        });
    }

    private void mapAccountTransaction(BankSession session, BankAccount account) {
        var response = enablebankingClient.getTransactions(account.getAccountUid());

        if(response == null || response.getTransactions() == null) return;

        response.getTransactions().forEach(tx -> {
            TransactionRawEvent event = TransactionRawEvent.builder()
                    .sessionId(session.getSessionId())
                    .accountId(account.getAccountUid())
                    .externalId(tx.getTransactionId())
                    .currency(tx.getTransactionAmount().getCurrency())
                    .amount(tx.getTransactionAmount().getAmount())
                    .creditor(tx.getCreditor())
                    .debtor(tx.getDebtor())
                    .type(tx.getType())
                    .bookingDate(tx.getBookingDate())
                    .status(tx.getStatus())
                    .build();

            kafkaPublisherService.publishTransactionEvent(event);
        });
    }
}
