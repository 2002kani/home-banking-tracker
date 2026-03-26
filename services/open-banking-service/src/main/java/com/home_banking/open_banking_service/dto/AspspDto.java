package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AspspDto {
    private String name;
    private String country;

    @JsonProperty("auth_methods")
    private List<String> authMethods;
}
