package com.example.dits.service;

import com.example.dits.entity.User;

import java.util.List;

public interface UserService {
    User getUserByLogin(String login);
    User getUserByUserId(int id);
    List<User> findAll();
    void save(User user);
    void update(User user,int id);
    void delete(User user);
}