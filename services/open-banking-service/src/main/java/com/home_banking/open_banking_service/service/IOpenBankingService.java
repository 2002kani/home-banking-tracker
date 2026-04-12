package com.home_banking.open_banking_service.service;

public interface IOpenBankingService{
    public String authAndSave(String code, String state, String bankName, String bankCountry);
}
