package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class ConsumerDemo {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        properties.put("group.id", "group1");
        properties.put("auto.offset.reset", "earliest");




        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
        kafkaConsumer.subscribe(Arrays.asList("topic1"));


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
//                System.out.println("Polling");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
                consumerRecords.forEach(record -> {
                    //System.out.println("Record Key "+record.key());
                    //System.out.println("Record Value "+record.value());
                    System.out.println("Record Partition " + record.partition());
                    System.out.println("Record Offset " + record.offset());
                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                // persist external data-sources
                // invoke api-calls

            }
        } catch (WakeupException e) {
            System.out.println("Wake up exception! " + e);
        } catch (Exception e) {
            System.out.println("Unexpected exception " + e);
        } finally {
            kafkaConsumer.close();
            System.out.println("The consumer is now gracefully closed");
        }


    }
}
