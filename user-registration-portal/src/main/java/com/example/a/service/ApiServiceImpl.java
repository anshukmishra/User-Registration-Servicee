package com.example.a.service;

import com.example.a.entity.ApiLog;
import com.example.a.repository.JdbcApiRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApiServiceImpl implements ApiService{

    private final JdbcApiRepository apiRepository;

    public ApiServiceImpl(JdbcApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void addApiLog(ApiLog apiLog) throws IOException{
        apiRepository.addApiLog(apiLog);
    }
}
