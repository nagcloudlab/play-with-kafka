package com.example;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
public class TransactionService {

    private  String topic;
    private final KafkaProducer<TransactionKey, Transaction> transactionKafkaProducer;

    @SneakyThrows
    public void sendTransactionEvent(Transaction transaction) {
        ProducerRecord<TransactionKey, Transaction> producerRecord = new ProducerRecord<>(topic, buildKey(transaction), transaction);
        transactionKafkaProducer.send(producerRecord).get();
    }

    private TransactionKey buildKey(Transaction transaction) {
        return TransactionKey.builder()
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .build();
    }
}
