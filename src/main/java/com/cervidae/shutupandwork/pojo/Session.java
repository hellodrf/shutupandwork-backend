package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Session {

    public enum Status {WAITING, ACTIVE, PAUSED, FINISHED, FAILED}

    private String id; // a 6 digit number set by users (-1 means not initialised)

    private Map<String, User> userList;

    private Status status;

    private User failSource;

    public Session(User startUser, String id) {
        this.userList = new ConcurrentHashMap<>();
        this.userList.put(startUser.getUsername(), startUser);
        this.id = id;
    }

    public boolean addUser(User user) {
        if (userList.containsKey(user.getUsername())) {
            return false;
        } else {
            userList.put(user.getUsername(), user);
            return true;
        }
    }
}
