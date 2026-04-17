package com.home_banking.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthMethodDto {
    @JsonProperty("psu_type")
    private PsuTypes psuType;
}
