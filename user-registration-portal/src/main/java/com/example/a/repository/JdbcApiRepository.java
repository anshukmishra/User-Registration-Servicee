package com.example.a.repository;

import com.example.a.entity.ApiLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcApiRepository implements ApiRepository{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcApiRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //TODO: add retry in all methods case failure happens, no. of retry = 2: Done
    @Override
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void addApiLog(ApiLog apiLog) throws IOException{
        String sql = "INSERT INTO apirepo (requestTimeStamp, method, reqPath, requestBody, resStatus, responseBody) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    apiLog.getRequestTimeStamp(),
                    apiLog.getMethod(),
                    apiLog.getReqPath(),
                    apiLog.getRequestBody(),
                    apiLog.getResStatus(),
                    apiLog.getResponseBody());
    }

    private static class ApiRowMapper implements RowMapper<ApiLog> {
        @Override
        public ApiLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            ApiLog apiLog = new ApiLog();
            apiLog.setId(rs.getLong("id"));
            apiLog.setRequestTimeStamp(rs.getTimestamp("requestTimeStamp"));
            apiLog.setMethod(rs.getString("method"));
            apiLog.setReqPath(rs.getString("reqPath"));
            apiLog.setResStatus(rs.getInt("resStatus"));
            apiLog.setResponseBody(rs.getString("responseBody"));
            return apiLog;
        }
    }
}
