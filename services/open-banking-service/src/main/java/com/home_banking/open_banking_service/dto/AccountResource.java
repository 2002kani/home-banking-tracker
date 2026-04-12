package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountResource {
    @JsonProperty("account_id")
    private AccountIdentifyDto accountId;

    private String name;

    private String currency;

    @JsonProperty("identification_hash")
    private String identificationHash;
}
