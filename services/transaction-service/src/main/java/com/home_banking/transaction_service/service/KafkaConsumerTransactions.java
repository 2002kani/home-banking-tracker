package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerTransactions {
    private final TransactionService transactionService;

    @KafkaListener(topics = "${kafka.topics.transactions-raw}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransactions(TransactionEvent event) {
        transactionService.updateTransactions(event);
    }
}
