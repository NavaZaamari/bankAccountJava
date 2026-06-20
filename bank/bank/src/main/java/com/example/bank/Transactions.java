package com.example.bank;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity 
@Table(name="TRANSACTIONS")
public class Transactions {

    @Id
    @GeneratedValue
    private long id;

    @Column(name="TYPE")
    private String type;

    @Column(name="TIME_STAMP")
    private LocalDateTime timestamp;

    @Column(name="AMOUNT")
    private double amount;

    @Column(name="ACCOUNT_ID")
    private UUID accountId;

    public Transactions(String type, UUID accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
