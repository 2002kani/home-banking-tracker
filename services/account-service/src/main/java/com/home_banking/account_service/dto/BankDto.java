package com.home_banking.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BankDto {
    private String name;
    private String country;

    @JsonProperty("auth_methods")
    private List<AuthMethodDto> authMethods;
}
