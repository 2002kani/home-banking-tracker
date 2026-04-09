package com.home_banking.open_banking_service.enums;

public enum PsuTypes {
    BUSINESS("business"),
    PERSONAL("personal");

    private final String value;

    PsuTypes(String value) {
        this.value = value;
    }
}

