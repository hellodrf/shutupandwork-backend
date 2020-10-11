package com.cervidae.shutupandwork.service;


import com.cervidae.shutupandwork.dao.UserMapper;
import com.cervidae.shutupandwork.pojo.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author AaronDu
 */
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
     * Get user by id, will return null when user does not exist.
     * @param id id of the user
     * @return the user
     */
    public User getByID(int id) {
        return userMapper.getById(id);
    }

    /**
     * Get user by username, will throw IllegalArgumentException when user does not exist.
     * @param id id of the user
     * @return the user
     */
    public User getByIDNotNull(int id) {
        User user = userMapper.getById(id);
        Assert.notNull(user, "3001");
        return user;
    }

    public User register(String username, String password) {
        ByteSource salt = ByteSource.Util.bytes(username);
        String hashedPassword = new SimpleHash("MD5", password, salt, 1024).toHex();

        // user must not exist in database
        User user = userMapper.getByUsername(username);
        Assert.isNull(user, "3002");

        long currentTime = System.currentTimeMillis();
        userMapper.register(username, hashedPassword, currentTime, "user");
        return userMapper.getByUsername(username);
    }

    /**
     * Update a user's score (with optimistic lock)
     * @param username username of the user
     * @param score new score
     */
    public void update(int id, String username, int score) {
        long updated = getByIDNotNull(id).getUpdated();
        userMapper.updateOptimistic(id, username, score, updated, System.currentTimeMillis());
    }
}
