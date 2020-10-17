package com.cervidae.shutupandwork.service;


import com.cervidae.shutupandwork.dao.ICache;
import com.cervidae.shutupandwork.dao.UserMapper;
import com.cervidae.shutupandwork.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
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

    private final ICache<User> userICache;

    private final ICache<String> usernameICache;

    @Autowired
    public UserService(UserMapper userMapper, ICache<User> userICache, ICache<String> usernameICache) {
        this.userMapper = userMapper;
        this.userICache = userICache;
        this.usernameICache = usernameICache;
        userICache.setIdentifier(4);
        usernameICache.setIdentifier(5);
    }

    /**
     * Get current logged in user's username
     * @return username
     */
    public String getCurrentUsername() {
        Subject subject = SecurityUtils.getSubject();
        return (String) subject.getPrincipal();
    }

    /**
     * Get user by username, will return null when user does not exist.
     * @param username username
     * @return the user
     */
    public User getByUsername(String username) {
        User cachedUser = userICache.select(username);
        if (cachedUser != null) {
            return cachedUser;
        }
        User dbUser = userMapper.getByUsername(username);
        if (dbUser != null) {
            userICache.put(username, dbUser);
            userICache.setExpiry(username, 5);
        }
        return dbUser;
    }

    /**
     * Get user by username, will throw IllegalArgumentException when user does not exist.
     * @param username username
     * @return the user
     */
    public User getByUsernameNotNull(String username) {
        User user = getByUsername(username);
        Assert.notNull(user, "3001");
        return user;
    }

    public User getByUsernameNoCache(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * Get user by id, will return null when user does not exist.
     * @param id id
     * @return the user
     */
    public User getByID(int id) {
        String username = usernameICache.select(String.valueOf(id));
        if (username != null) {
            User cachedUser = userICache.select(username);
            if (cachedUser != null) {
                return cachedUser;
            }
        }
        User dbUser = userMapper.getById(id);
        if (dbUser != null) {
            username = dbUser.getUsername();
            usernameICache.put(String.valueOf(id), username);
            usernameICache.setExpiry(String.valueOf(id), 120);
            userICache.put(username, dbUser);
            userICache.setExpiry(username, 5);
        }
        return dbUser;
    }

    /**
     * Get user by username, will throw IllegalArgumentException when user does not exist.
     * @param id id
     * @return the user
     */
    public User getByIDNotNull(int id) {
        User user = getByID(id);
        Assert.notNull(user, "3001");
        return user;
    }

    /**
     * Register a new user into database
     * @param username username
     * @param password password
     * @return the new user
     */
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
     * Update a user's score
     * @param username username
     * @param score new score
     */
    public void updateScore(String username, int score) {
        long updated = getByUsernameNotNull(username).getUpdated();
        userICache.drop(username);
        userMapper.updateScoreOptimistic(username, score, updated, System.currentTimeMillis());
    }

    @Deprecated
    public void updateUsername(int id, String oldUsername, String username) {
        long updated = getByIDNotNull(id).getUpdated();
        Assert.isNull(getByUsername(username), "3005");
        userICache.drop(oldUsername);
        usernameICache.drop(String.valueOf(id));
        userMapper.updateUsernameOptimistic(id, username, updated, System.currentTimeMillis());
    }
}
