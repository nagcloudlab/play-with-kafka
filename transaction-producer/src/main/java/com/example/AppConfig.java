package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public TransactionService transactionProducer(@Value("${transaction-producer.topic}") String topic,
                                                  KafkaProducer<TransactionKey, Transaction> kafkaProducer) {
        return new TransactionService(topic, kafkaProducer);
    }

}
