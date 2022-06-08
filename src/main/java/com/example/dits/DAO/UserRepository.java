package com.example.dits.DAO;

import com.example.dits.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
     User getUserByLogin(String login);

     List<User> findAll();
}
