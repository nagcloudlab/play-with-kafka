package com.example;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.metrics.stats.Value;

public class ProducerInterceptor implements org.apache.kafka.clients.producer.ProducerInterceptor<String, Value> {

    @Override
    public ProducerRecord<String, Value> onSend(ProducerRecord<String, Value> record) {
        System.out.println("ProducerInterceptor.onSend");
        return record;
    }

    @Override
    public void onAcknowledgement(org.apache.kafka.clients.producer.RecordMetadata metadata, Exception exception) {
        System.out.println("ProducerInterceptor.onAcknowledgement");
    }

    @Override
    public void close() {
        System.out.println("ProducerInterceptor.close");
    }

    @Override
    public void configure(java.util.Map<java.lang.String, ?> configs) {
        System.out.println("ProducerInterceptor.configure");
    }
}
