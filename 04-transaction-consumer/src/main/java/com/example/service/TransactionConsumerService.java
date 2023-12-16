package com.example.service;

import com.example.domain.Transaction;
import com.example.domain.TransactionKey;
import com.example.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class TransactionConsumerService /*implements AcknowledgingMessageListener<TransactionKey,Transaction> */ {

    private final TransactionRepository transactionRepository;

    @KafkaListener(
            groupId = "transaction-consumer-group",
            topics = "transactions",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void onNewTransaction(ConsumerRecord<TransactionKey, Transaction> consumerRecord) {
        System.out.println("Received new transaction: " + consumerRecord.value());

//        Transaction transaction=consumerRecord.value();
//        System.out.println(transaction);


        validate(consumerRecord.value());

        com.example.entity.Transaction transaction = new com.example.entity.Transaction();
        transaction.setId(consumerRecord.value().getId());
        transaction.setType(consumerRecord.value().getType());
        transaction.setAmount(consumerRecord.value().getAmount());
        Instant instant = consumerRecord.value().getTimestamp().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        transaction.setTimestamp(date);
        transaction.setFromAccount(consumerRecord.value().getFromAccount());
        transaction.setToAccount(consumerRecord.value().getToAccount());
        transactionRepository.save(transaction);

    }

    private void validate(Transaction transaction) {
        if (transaction.getFromAccount() == null) {
            throw new IllegalArgumentException("From account is null");
        }
        if (transaction.getToAccount() == null) {
            throw new IllegalStateException("To account is null");
        }
    }

//    @Override
//    @KafkaListener(
//            topics = "transactions",
//            containerFactory = "kafkaListenerContainerFactory"
//    )
//    public void onMessage(ConsumerRecord<TransactionKey, Transaction> consumerRecord, Acknowledgment acknowledgment) {
//        System.out.println("Received new transaction: " + consumerRecord);
//        acknowledgment.acknowledge();
//    }




}
