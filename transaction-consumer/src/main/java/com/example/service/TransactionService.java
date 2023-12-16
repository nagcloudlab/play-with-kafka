package com.example.service;

import com.example.domain.Transaction;
import com.example.domain.TransactionKey;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

    @KafkaListener(topics = "transactions")
    public void onNewTransaction(ConsumerRecord<TransactionKey, Transaction> consumerRecord) {
        System.out.println("Received new transaction: " + consumerRecord.value());
    }

}
