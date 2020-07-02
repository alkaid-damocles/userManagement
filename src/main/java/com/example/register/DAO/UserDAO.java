package com.example.register.DAO;

import com.example.register.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {
    void save(User user);

    User findByUsername(String username);

    List<User> findAll();

    List<User> findByPage(@Param("start") Integer start, @Param("rows") Integer rows);

    Integer findTotals();

    void update(User user);

    void delete(String id);

    User findById(String id);
}
