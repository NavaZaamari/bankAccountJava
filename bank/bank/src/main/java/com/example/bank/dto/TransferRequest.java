package com.example.bank.dto;

public record TransferRequest(String toCardNumber, double amount) {
}
