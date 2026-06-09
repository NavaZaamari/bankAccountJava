package com.example.bank.dto;

public record AccountResponse(Long id, String accountHolder, double balance) {

    public double getBalance() {
        return balance;
    }
}
