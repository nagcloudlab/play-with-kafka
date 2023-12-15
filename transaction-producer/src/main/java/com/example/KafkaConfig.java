package com.example;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@EnableKafka
@Configuration
public class KafkaConfig {

//    @Bean
//    public RecordMessageConverter recordMessageConverter() {
//        return new ByteArrayJsonMessageConverter();
//    }

    @Bean
    public KafkaProducer<TransactionKey, Transaction> kafkaProducer(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer<>(props);
    }


    // Configure the listener container factory for parallel consumption
    @Bean("cf")
    public ConcurrentKafkaListenerContainerFactory<TransactionKey, Transaction> kafkaListenerContainerFactory(
            ConsumerFactory<TransactionKey, Transaction> kafkaConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<TransactionKey, Transaction> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        //factory.setConcurrency(3); // Set the number of consumer threads
        return factory;
    }

}
