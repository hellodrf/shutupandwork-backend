package com.cervidae.shutupandwork.pojo;

import com.cervidae.shutupandwork.util.Constants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This data will be stored in: MEMORY
 */

@Data
public class Session {

    /**
     * Status of the session
     */
    public enum Status {WAITING, ACTIVE, PAUSE, SUCCESS, FAIL}

    @Setter(AccessLevel.PRIVATE)
    private Status status;

    /**
     * ID set by user (a 6 digit number)
     */
    private String sessionID;

    /**
     * List of Users in session
     */
    @Setter(AccessLevel.PRIVATE)
    private Map<String, User> userList; // List of all users in ConcurrentHashMap

    /**
     * Timestamps in format of UNIX timestamp
     */
    private long created;

    private long started;

    private long target;

    /**
     * Blamable user
     */
    private User failSource;

    /**
     * Set created timestamp, and status of WAITING.
     * @param startUser the user who created this session
     * @param sessionID sessionID
     */
    public Session(User startUser, String sessionID) {
        this.userList = new ConcurrentHashMap<>();
        this.userList.put(startUser.getUsername(), startUser);
        this.sessionID = sessionID;
        this.status = Status.WAITING;
        this.created = System.currentTimeMillis()/1000;
    }

    /**
     * Add a user to userList (idempotent action)
     * @param user user to be added
     */
    public void addUser(User user) {
        if (!userList.containsKey(user.getUsername())) {
            userList.put(user.getUsername(), user);
        }
    }

    /**
     * Remove a user, will validate user before removing (idempotent action)
     * @param user user to be added
     */
    public void removeUser(User user) {
        userList.remove(user.getUsername(), user);
    }


    /*
     * Below are Pessimistic locked actions.
     * Using "synchronised" since we are manipulating memory values, and we are not distributed.
     */


    /**
     * Start the session
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * @param target the target of the session
     */
    public synchronized Session start(long target) {
        if (getStatus() != Status.WAITING) {
            throw new IllegalArgumentException("session is not in waiting state");
        }
        this.setTarget(target);
        this.setStarted(System.currentTimeMillis()/1000);
        this.setStatus(Session.Status.ACTIVE);
        return this;
    }

    /**
     * Mark the session as succeed
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     */
    public synchronized Session success() {
        if (getStatus()!= Session.Status.ACTIVE) {
            throw new IllegalArgumentException("session is not in active state");
        }
        if ((this.started+this.target) - System.currentTimeMillis()/1000 < Constants.sessionSuccessThreshold) {
            this.setStatus(Session.Status.SUCCESS);
        } else {
            throw new IllegalArgumentException("session has not reach its target, maximum threshold: "
                    + Constants.sessionSuccessThreshold + " seconds");
        }
        return this;
    }

    /**
     * Mark the session as failed
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * @param user the user to blame
     */
    public synchronized Session fail(User user) {
        if (getStatus()!= Session.Status.ACTIVE) {
            throw new IllegalArgumentException("session is not in active state");
        }
        this.setFailSource(user);
        this.setStatus(Session.Status.FAIL);
        return this;
    }
}
