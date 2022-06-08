package com.example.dits.service.impl;

import com.example.dits.DAO.UserRepository;
import com.example.dits.entity.User;
import com.example.dits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User getUserByLogin(String login){
        return repository.getUserByLogin(login);
    }


    @Transactional
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(User user) {
        repository.save(user);
    }
}
