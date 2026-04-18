package com.home_banking.account_service.service;

import com.home_banking.account_service.event.AccountUpdateEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer implements IKafkaConsumer {
    private final AccountService accountService;

    @KafkaListener(topics = "${kafka.topics.account-update}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void handleAccountUpdate(AccountUpdateEvent event) {
        accountService.updateAccount(event);
    }
}
