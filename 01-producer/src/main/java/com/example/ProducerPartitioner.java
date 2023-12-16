package com.example;

import org.apache.kafka.common.Cluster;

import java.util.Map;

public class ProducerPartitioner implements org.apache.kafka.clients.producer.Partitioner{


    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //..
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
