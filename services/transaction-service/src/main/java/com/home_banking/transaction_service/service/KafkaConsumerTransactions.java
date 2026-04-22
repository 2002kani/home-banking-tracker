package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerTransactions {
    private final TransactionService transactionService;

    /*
    /* In future may add Batch Mode as List<Transactions> for performance
    */
    @KafkaListener(topics = "${kafka.topics.transactions-raw}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransactions(TransactionEvent event) {
        transactionService.persistTransactions(event);
    }
}
