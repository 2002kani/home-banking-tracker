package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessDto {
    @JsonProperty("valid_until")
    private String validUntil;
}
