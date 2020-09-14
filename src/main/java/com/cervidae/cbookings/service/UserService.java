package com.cervidae.cbookings.service;


import com.cervidae.cbookings.dao.UserMapper;
import com.cervidae.cbookings.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public Set<String> getRoles(String username) {
        return userMapper.getRoles(username);
    }

    public Set<String> getPermissions(String username) {
        return userMapper.getRoles(username);
    }

    public Boolean add(String username, String password, String name, String address,
                       String phone, String email, String extra, int roleId) {
        return userMapper.add(username, password, name, address, phone, email, extra, roleId);
    }
}
