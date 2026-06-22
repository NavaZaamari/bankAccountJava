package com.example.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransaction(String message) {
        kafkaTemplate.send("transactions", message);
    }
}
