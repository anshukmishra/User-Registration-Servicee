package com.example.a.repository;

import com.example.a.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import javax.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {


    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM userrepo";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM userrepo WHERE id = ?";
        try{
            User x=jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
            return x;
        }
        catch(Exception e){
            return new User();
        }

    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM userrepo WHERE username = ?";
        try{
            User x=jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
            return x;
        }
        catch(Exception e){
            return new User();
        }
    }

    @Override
    @Retryable(value = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public void addUser(User user) {
        String sql = "INSERT INTO userrepo (username, password, firstName, lastName, email, phoneNo, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Date now = new Date();
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNo(),
                now,
                now);
    }

    @Override
    @Retryable(value = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public void updateUser(User user) {
        String sql = "UPDATE userrepo SET username=?, password=?, firstName=?, lastName=?, email=?, phoneNo=?, updatedAt=? " +
                "WHERE id=?";
        Date now = new Date();
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNo(),
                now,
                user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        String sql = "DELETE FROM userrepo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMapper to map rows from the database to User objects
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNo(rs.getString("phoneNo"));
            user.setCreatedTime(rs.getTimestamp("createdAt"));
            user.setUpdatedTime(rs.getTimestamp("updatedAt"));
            return user;
        }
    }




}

