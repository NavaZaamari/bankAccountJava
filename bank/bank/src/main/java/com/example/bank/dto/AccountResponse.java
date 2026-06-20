package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record AccountResponse(String cardNumber, java.util.UUID id, String accountHolder, double balance) {


    public double getBalance() {
        return balance;
    }
}
