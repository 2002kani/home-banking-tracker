package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home_banking.open_banking_service.enums.PsuTypes;
import lombok.Data;

@Data
public class AuthMethodDto {
    @JsonProperty("psu_type")
    private PsuTypes psuType;
}
