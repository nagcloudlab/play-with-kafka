package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
//@EnableKafka
public class TransactionConsumer {

    @KafkaListener(topics = {"transactions"},containerFactory = "cf")
    public void onNewTransaction(
            ConsumerRecord<TransactionKey,Transaction> consumerRecord
    ){
        System.out.println("Received Record: "+consumerRecord);
    }

}
