package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Session {

    public enum Status {WAITING, ACTIVE, PAUSED, FINISHED, FAILED}

    private String id; // a 6 digit number set by users (-1 means not initialised)

    private Map<String, User> userList; // List of all users in ConcurrentHashMap

    private long created;

    private long started;

    private long target;

    private Status status;

    private User failSource;

    public Session(User startUser, String id) {
        this.userList = new ConcurrentHashMap<>();
        this.userList.put(startUser.getUsername(), startUser);
        this.id = id;
        this.status = Status.WAITING;
        this.created = System.currentTimeMillis()/1000;
    }

    public boolean addUser(User user) {
        if (userList.containsKey(user.getUsername())) {
            return false;
        } else {
            userList.put(user.getUsername(), user);
            return true;
        }
    }

    public void setStatus(Status status) {
        if (status==Status.ACTIVE && this.status!=Status.ACTIVE) {
            this.started = System.currentTimeMillis()/1000;
            this.status = Status.ACTIVE;
        }
    }

}
