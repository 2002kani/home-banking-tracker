package com.home_banking.account_service.service;

import com.home_banking.account_service.event.AccountUpdateEvent;

public interface IKafkaConsumer {
    public void handleAccountUpdate(AccountUpdateEvent event);
}
