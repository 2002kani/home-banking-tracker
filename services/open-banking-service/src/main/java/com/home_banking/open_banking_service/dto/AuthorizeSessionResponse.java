package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home_banking.open_banking_service.enums.PsuTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AuthorizeSessionResponse {
    @JsonProperty("session_id")
    private String sessionId;

    private List<AccountResource> accounts; // Information only shown once!

    private AspspMinDto aspsp;

    @JsonProperty("psu_type")
    private PsuTypes psuType;

    private AccessDto  access;
}
