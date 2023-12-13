package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;


public class ConsumerDemo {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        properties.put("group.id","");
        properties.put("auto.offset.reset","earliest");
        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer(properties);

        kafkaConsumer.subscribe(Arrays.asList("topic1"));

        try{
            while(true){
                System.out.println("Polling");
               ConsumerRecords<String,String> consumerRecords= kafkaConsumer.poll(1000);
                consumerRecords.forEach(record -> {
                     //System.out.println("Record Key "+record.key());
                     //System.out.println("Record Value "+record.value());
                     System.out.println("Record Partition "+record.partition());
                     System.out.println("Record Offset "+record.offset());
                });
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }


    }
}
