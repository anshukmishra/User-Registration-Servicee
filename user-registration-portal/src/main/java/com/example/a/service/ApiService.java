package com.example.a.service;

import com.example.a.entity.ApiLog;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;

//TODO: why interface? :- Maybe to make derived class such that this functionality must be called
public interface ApiService {
    public void addApiLog(ApiLog apiLog) throws IOException;
}
