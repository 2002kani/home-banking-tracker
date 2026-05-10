package com.home_banking.auth_service.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER;

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
