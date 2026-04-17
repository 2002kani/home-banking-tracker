package com.home_banking.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BanksListResponse {
    @JsonProperty("aspsps")
    private List<BankDto> banks;
}
