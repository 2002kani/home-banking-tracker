package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.event.TransactionRawEvent;

public interface IKafkaPublisherService {
    public void publishTransactionEvent(TransactionRawEvent event);
}
