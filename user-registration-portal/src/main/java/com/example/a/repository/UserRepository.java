package com.example.a.repository;

import com.example.a.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
}
