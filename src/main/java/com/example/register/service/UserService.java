package com.example.register.service;

import com.example.register.entity.user.User;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserService {
    void register(User user);

    User login(User user);

    List<User> findAll();

    List<User> findByPage(Integer page, Integer rows);

    Integer findTotals();

    void update(User user);

    void delete(String id);

    User findById(String id);
}
