package com.example.a.entity;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class User {
    private Long id;

    @NotEmpty(message="UserName Can't be empty")
    private String username;

    @NotEmpty(message="Password Can't be empty")
    private String password;

    @Email(message="Email should be valid")
    private String email;

    private String phoneNo;

    @NotEmpty(message="First Name Can't be empty")
    private String firstName;

    private String lastName;

    private Timestamp createdTime;

    private Timestamp updatedTime;
}
