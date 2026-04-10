package com.home_banking.open_banking_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PsuTypes {
    BUSINESS("business"),
    PERSONAL("personal");

    private final String value;

    PsuTypes(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PsuTypes fromValue(String value) {
        for (PsuTypes type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PSU type: " + value);
    }
}

