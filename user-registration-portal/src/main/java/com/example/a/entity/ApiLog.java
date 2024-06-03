package com.example.a.entity;

import lombok.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class ApiLog {
    private Long id;

    private Timestamp requestTimeStamp;

    private String method;

    private String reqPath;

    private StringBuilder requestBody;

    private Integer resStatus;

    private String responseBody;
}

