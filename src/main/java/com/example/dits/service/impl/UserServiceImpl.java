package com.example.dits.service.impl;

import com.example.dits.DAO.UserRepository;
import com.example.dits.entity.User;
import com.example.dits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public User getUserByUserId(int id) {
        return repository.getUserByUserId(id);
    }

    @Transactional
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(User user) {
        repository.save(user);
    }

    @Transactional
    public void update(User userUpdate,int id) {
        Optional<User> u = repository.findById(id);
        if(u.isPresent()){
            User user = updateUserInfo(userUpdate,u.get());
            repository.save(user);
        }
    }

    @Transactional
    public void delete(User user) {
        repository.delete(user);
    }

    private User updateUserInfo(User userUpdate,User user){
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setRole(userUpdate.getRole());
        user.setLogin(userUpdate.getLogin());
        user.setPassword(userUpdate.getPassword());
        return user;
    }
}
