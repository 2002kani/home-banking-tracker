package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.event.TransactionRawEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPublisherService implements IKafkaPublisherService{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("kafka.topics.transactions-raw")
    private String transactionTopic;

    @Override
    public void publishTransactionEvent(TransactionRawEvent event) {
        kafkaTemplate.send(transactionTopic, event.getSessionId(), event);  // sessionId as key, so any event per session are stored in same partition
    }
}
