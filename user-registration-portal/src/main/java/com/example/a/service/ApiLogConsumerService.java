package com.example.a.service;

import com.example.a.entity.ApiLog;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class ApiLogConsumerService {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    private ApiServiceImpl apiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLogConsumerService.class);
    @Value("${kafka.topic}")
    private String topic;


    public void consume() {
        kafkaConsumer.subscribe(Collections.singleton(topic));

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                // Process the Kafka message
                String key = record.key();
                String value = record.value();
                LOGGER.info("key", key);
                LOGGER.info("value", value);
                Gson gson = new Gson();
                try {
                    ApiLog apiLog = gson.fromJson(value, ApiLog.class);
                    apiService.addApiLog(apiLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

