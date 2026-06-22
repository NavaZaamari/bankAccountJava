package com.example.bank;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @KafkaListener(topics = "transactions")
    public void consume(String message) {
        System.out.println(message);
    }
}
