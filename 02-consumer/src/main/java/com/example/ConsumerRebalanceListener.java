package com.example;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;

public class ConsumerRebalanceListener implements org.apache.kafka.clients.consumer.ConsumerRebalanceListener {

    private KafkaConsumer<String, String> kafkaConsumer;
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets;

    public ConsumerRebalanceListener(KafkaConsumer<String, String> kafkaConsumer, Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        this.kafkaConsumer = kafkaConsumer;
        this.currentOffsets = currentOffsets;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
        // clean up the state of the consumer
        kafkaConsumer.commitSync(currentOffsets);
        currentOffsets.clear();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
        // state initialization of the consumer
    }

    @Override
    public void onPartitionsLost(Collection<TopicPartition> partitions) {
        org.apache.kafka.clients.consumer.ConsumerRebalanceListener.super.onPartitionsLost(partitions);
    }
}
