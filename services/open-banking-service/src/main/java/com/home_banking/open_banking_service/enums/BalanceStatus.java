package com.home_banking.open_banking_service.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BalanceStatus {
    CLAV,
    CLBD,
    FWAV,
    INFO,
    ITAV,
    ITBD,
    OPAV,
    OPBD,
    OTHR;

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
