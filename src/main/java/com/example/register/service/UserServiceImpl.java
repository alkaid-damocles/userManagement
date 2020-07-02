package com.example.register.service;

import com.example.register.DAO.UserDAO;
import com.example.register.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public void register(User user) {
        if (userDAO.findByUsername(user.getUsername()) == null) {
            userDAO.save(user);
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }

    @Override
    public User login(User user) {
        var userDB = userDAO.findByUsername(user.getUsername());
        if (userDB == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!userDB.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return userDB;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(Integer page, Integer rows) {
        int start = (page - 1) * rows;
        return userDAO.findByPage(start, rows);
    }

    @Override
    public Integer findTotals() {
        return userDAO.findTotals();
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void delete(String id) {
        userDAO.delete(id);
    }

    @Override
    public User findById(String id) {
        return userDAO.findById(id);
    }

}
