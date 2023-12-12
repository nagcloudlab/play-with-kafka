package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ProducerDemo {
    public static void main(String[] args) throws InterruptedException {

        Properties properties = new Properties();
        properties.put("client.id", "producer-demo");
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //properties.put("batch.size", 1*16384); // 16k
        //properties.put("linger.ms", 0); // 1ms
        //properties.put("compression.type", "snappy");

//        properties.put("buffer.memory", 33554432); // 32MB
//        properties.put("max.block.ms", 60000); // 1ms


//        properties.put("acks", "all");
//        properties.put("max.in.flight.requests.per.connection", 5);

//         properties.put("request.timeout.ms", 30000);

         // ** Transient Failures ( Timeout, Network Exception, Leader Not Available, Not Enough Replicas)
//        properties.put("retries", Integer.MAX_VALUE);
//        properties.put("retry.backoff.ms", 1000);

//        properties.put("enable.idempotence", true);

//        properties.setProperty("delivery.timeout.ms", "120000");

//        properties.setProperty("connections.max.idle.ms", "540000");

//        properties.setProperty("max.request.size", "1048576"); // 1MB

//        properties.setProperty("interceptor.classes", "com.example.ProducerInterceptor");

//        properties.setProperty("transactional.id", "my-transactional-id");

//        properties.setProperty("partitioner.class", "com.example.ProducerPartitioner");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down producer...");
            producer.flush();
            producer.close();
        }));

        String value = "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation written in Java and Scala. The project aims to provide a unified, high-throughput, low-latency platform for handling real-time data feed\n" +
                "Apache Kafka is a distributed event store and stream-processing platform. It is an open-source system developed by the Apache Software Foundation write";

        for (int i = 0; i < 1000*10000; i++) {
            String key= i % 2==0?"even":"odd";
            ProducerRecord<String, String> record1 = new ProducerRecord<>("topic3", key, value);
            System.out.println("Sending message-"+i+" >>>>>>>>>>>>>>>>>");
            producer.send(record1, (recordMetadata, exception) -> {
                if (exception == null) {
                    System.out.println("<<<<<<<<<<<<<<Received new metadata \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    System.out.println("Error while producing: " + exception);
                }
            });
            //TimeUnit.MILLISECONDS.sleep(1);
        }


//        try{
//            producer.initTransactions();
//            producer.beginTransaction();
//            producer.send(new ProducerRecord<>("topic2", "record-1"));
//            producer.send(new ProducerRecord<>("topic2", "record-2"));
//            if(true){
//                throw new RuntimeException("oops");
//            }
//            producer.commitTransaction();
//        }catch (Exception e) {
//            producer.abortTransaction();
//        }


        producer.flush();
        producer.close();


    }
}
