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

    public Boolean add(String username, int score) {
        return userMapper.add(username, score);
    }

    public Boolean update(String username, int score) {
        return userMapper.update(username, score);
    }
}
