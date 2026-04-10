package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StartAuthResponse {
    private String url;

    @JsonProperty("authorization_id")
    private String authorizationId;
}
