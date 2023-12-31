package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Consumer {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        properties.put("group.id", "group1");
        properties.put("auto.offset.reset", "earliest");

        // properties.put("partition.assignment.strategy",
        // "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");
        // properties.put("assignment.consumer.priority", "3");
        // properties.put("partition.assignment.strategy","com.example.FailoverAssignorConfig");
        // properties.put("group.instance.id", args[0]);

        // properties.put("enable.auto.commit", "false");
        // properties.put("auto.commit.interval.ms", "5000");

        // properties.put("max.poll.interval.ms", "300000");
        // properties.put("fetch.min.bytes", "1");
        // properties.put("fetch.max.bytes", "52428800");
        // properties.put("max.poll.records", "500");
        // properties.put("max.partition.fetch.bytes", "1048576");
        // properties.put("heartbeat.interval.ms", "3000");
        // properties.put("session.timeout.ms", "45000");
        // properties.put("request.timeout.ms", "30000");
        // properties.put("client.id", "consumer-demo");

        // properties.put("client.rack", "rack1");
        // properties.put("group.initial.rebalance.delay.ms", "3000");

        Map<TopicPartition, OffsetAndMetadata> currentProcessedOffsets = new HashMap<>(); // or redis...

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
        kafkaConsumer.subscribe(Arrays.asList("topic4"),
                new ConsumerRebalanceListener(kafkaConsumer, currentProcessedOffsets));

        // get a reference to the current thread
        final Thread mainThread = Thread.currentThread();
        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Detected a shutdown, let's exit by calling consumer.wakeup()...");
            kafkaConsumer.wakeup();
            // join the main thread to allow the execution of the code in the main thread
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        try {
            while (true) {
                // System.out.println("Polling");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);//
                // System.out.println("Received Records : "+consumerRecords.count());
                consumerRecords.forEach(record -> {
                    // System.out.println("Record Key "+record.key());
                    // System.out.println("Record Value "+record.value());
                    // System.out.println("Record Partition " + record.partition());
                    System.out.println("Record Offset " + record.offset());
                    try {
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentProcessedOffsets.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "no metadata"));
                });
                kafkaConsumer.commitSync(currentProcessedOffsets);
                currentProcessedOffsets.clear();
            }
        } catch (WakeupException e) {
            System.out.println("Wake up exception! " + e);
        } catch (Exception e) {
            System.out.println("Unexpected exception " + e);
        } finally {
            kafkaConsumer.close(); // Leaving Request
            System.out.println("The consumer is now gracefully closed");
        }

    }
}
