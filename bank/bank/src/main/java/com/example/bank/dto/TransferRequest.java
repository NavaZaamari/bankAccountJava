package com.example.bank.dto;

public record TransferRequest(Long toAccountId, double amount) {
}
