package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema()
public record AuthRequest (String username, String password) {}
