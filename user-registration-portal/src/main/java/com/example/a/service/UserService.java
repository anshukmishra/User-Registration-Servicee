package com.example.a.service;

import com.example.a.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
}

