package com.example.dits.service;

import com.example.dits.entity.User;

import java.util.List;

public interface UserService {
    User getUserByLogin(String login);

    List<User> findAll();
}
