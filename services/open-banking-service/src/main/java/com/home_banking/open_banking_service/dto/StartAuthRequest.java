package com.home_banking.open_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAuthRequest {
    private AccessDto access;

    private AspspMinDto aspsp;

    private String state;

    @JsonProperty("redirect_url")
    private String redirectUrl;
}
