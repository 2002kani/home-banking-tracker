package com.home_banking.transaction_service.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CreditDebitIndicator {
    CRDT,
    DBIT;

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
