package com.cervidae.shutupandwork.service;


import com.cervidae.shutupandwork.dao.UserMapper;
import com.cervidae.shutupandwork.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Get user by username, will return null when user does not exist.
     * @param username username of the user
     * @return the user
     */
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * Get user by username, will throw IllegalArgumentException when user does not exist.
     * @param username username of the user
     * @return the user
     */
    public User getByUsernameNotNull(String username) {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("cannot find user in database");
        }
        return user;
    }

    /**
     * Add the user to database
     * @param username username of the user
     * @param score score of the user
     */
    public void add(String username, int score) {
        User user = userMapper.getByUsername(username);
        if (user != null) {
            throw new IllegalArgumentException("user already exists");
        }
        userMapper.add(username, score);
    }

    /**
     * Update a user's score (with optimistic lock)
     * @param username username of the user
     * @param score new score
     */
    public void update(String username, int score) {
        long version = getByUsername(username).getVersion();
        userMapper.updateOptimistic(username, score, version);
    }
}
