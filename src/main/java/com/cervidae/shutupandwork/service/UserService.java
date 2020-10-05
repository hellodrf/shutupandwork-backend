package com.cervidae.shutupandwork.service;


import com.cervidae.shutupandwork.dao.UserMapper;
import com.cervidae.shutupandwork.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User getById(int id) {
        return userMapper.findByID(id);
    }

    public void add(String username, int score) {
        userMapper.add(username, score);
    }

    public void update(String username, int score) {
        userMapper.update(username, score);
    }
}
