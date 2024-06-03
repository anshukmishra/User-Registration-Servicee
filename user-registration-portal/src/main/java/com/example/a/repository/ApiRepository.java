package com.example.a.repository;

import com.example.a.entity.ApiLog;

import java.io.IOException;

public interface ApiRepository {
    void addApiLog(ApiLog apiLog) throws IOException;
}
