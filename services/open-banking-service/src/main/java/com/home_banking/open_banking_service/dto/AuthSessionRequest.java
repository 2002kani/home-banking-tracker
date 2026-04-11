package com.home_banking.open_banking_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthSessionRequest {
    private String code;
}
