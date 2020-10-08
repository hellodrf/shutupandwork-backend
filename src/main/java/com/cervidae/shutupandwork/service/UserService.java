package com.cervidae.shutupandwork.service;


import com.cervidae.shutupandwork.dao.UserMapper;
import com.cervidae.shutupandwork.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


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
        // user must exist in database
        User user = userMapper.getByUsername(username);
        Assert.notNull(user, "3001");
        return user;
    }

    /**
     * Add the user to database
     * @param username username of the user
     * @param score score of the user
     */
    public void add(String username, int score) {
        // user must not exist in database
        User user = userMapper.getByUsername(username);
        Assert.isNull(user, "3002");

        long currentTime = System.currentTimeMillis();
        userMapper.add(username, score, currentTime);
    }

    /**
     * Update a user's score (with optimistic lock)
     * @param username username of the user
     * @param score new score
     */
    public void update(String username, int score) {
        long updated = getByUsername(username).getUpdated();
        long currentTime = System.currentTimeMillis();
        userMapper.updateOptimistic(username, score, updated, currentTime);
    }
}
