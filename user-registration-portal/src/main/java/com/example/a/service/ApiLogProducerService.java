package com.example.a.service;

import com.example.a.entity.ApiLog;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApiLogProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLog(ApiLog apiLogMessage) {
        Gson gson = new Gson();
        String logMessage = gson.toJson(apiLogMessage);
        kafkaTemplate.send("api-logs", logMessage);
    }
}

