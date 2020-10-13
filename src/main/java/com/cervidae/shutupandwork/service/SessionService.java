package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.pojo.Session;
import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.dao.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.logging.Logger;

/**
 * @author AaronDu
 */
@Service
public class SessionService implements IService {

    final ICache<Session> iCache;

    final UserService userService;

    @Autowired
    public SessionService(ICache<Session> iCache, UserService userService) {
        this.userService = userService;
        this.iCache = iCache;
        this.iCache.setIdentifier(2);
    }

    /**
     * Validate sessionID using Regex (Util.Constants.sessionIDRegex)
     * @param sessionID the session id
     */
    private void validateID(String sessionID) {
        // validate sessionID
        Assert.isTrue(sessionID.matches(Constants.SESSION_ID_REGEX), "4005");
    }

    /**
     * Remove expired sessions (see Constants.SESSION_EXPIRY)
     * Cron task scheduled 3am everyday
     */
    @Scheduled(cron = "00 03 * * * ?")
    public void collectExpiredSessions() {
        int i = 0;
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Starting Session GC");
        Set<String> sessionIDs = iCache.getKeySet();
        for (String id : sessionIDs) {
            Session session = iCache.select(id);
            if (session.getStatus() != Session.Status.ACTIVE &&
                    System.currentTimeMillis() - session.getCreated() > Constants.SESSION_EXPIRY) {
                iCache.drop(id);
                logger.info("Session GC: dropped session " + id);
                i++;
            }
        }
        logger.info("Session GC Completed: dropped " + i + " sessions");
    }

    /**
     * Join the specified session. If not exist, create a session
     * @param user the user
     * @param sessionID ID of the session
     * @return the session
     */
    public Session join(User user, String sessionID) {
        validateID(sessionID);
        Session session;
        if (iCache.contains(sessionID)) {
            session = iCache.select(sessionID);
            if (session.getStatus() == Session.Status.SUCCESS || session.getStatus() == Session.Status.FAIL) {
                session.reset();
            } else if (session.getStatus() != Session.Status.WAITING) {
                throw new IllegalArgumentException("4001");
            }
            session.addUser(user);
        } else {
            session = new Session(user, sessionID);
        }
        iCache.put(sessionID, session);
        return session;
    }

    /**
     * Leave the specified session.
     * @param user the user
     * @param sessionID ID of the session
     */
    public void leave(User user, String sessionID) {
        validateID(sessionID);
        Session session = getSession(sessionID);
        if (session.getUserList().containsKey(user.getUsername())) {
            if (session.getStatus()== Session.Status.ACTIVE) {
                session.fail(user);
            } else {
                session.removeUser(user);
            }
        }
        iCache.put(sessionID, session);
    }

    /**
     * Get a specific session with sessionID, nullable
     * @param sessionID ID of the session
     * @return the session
     */
    public Session getSession(String sessionID) {
        return iCache.select(sessionID);
    }

    /**
     * Get a specific session with sessionID, not nullable (will throw exception)
     * @param sessionID ID of the session
     * @return the session
     */
    public Session getSessionNotNull(String sessionID) {
        Assert.isTrue(iCache.contains(sessionID), "4004");
        return iCache.select(sessionID);
    }

    /**
     * Start the session (Pessimistic lock, see Session)
     * @param sessionID ID of the session
     * @param target the target of the session
     */
    public Session start(String sessionID, long target) {
        validateID(sessionID);
        Session session = getSessionNotNull(sessionID).start(target);
        iCache.put(sessionID, session);
        return session;
    }

    /**
     * Mark the session as succeed (Pessimistic lock, see Session)
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * @param sessionID ID of the session
     */
    public Session success(String sessionID) {
        validateID(sessionID);
        Session session = getSessionNotNull(sessionID).success();
        iCache.put(sessionID, session);
        return session;
    }

    /**
     * Mark the session as failed (Pessimistic lock, see Session)
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * @param sessionID ID of the session
     * @param username user to blame
     */
    public Session fail(String sessionID, String username) {
        validateID(sessionID);
        User user = userService.getByUsername(username);
        Session session = getSessionNotNull(sessionID).fail(user);
        iCache.put(sessionID, session);
        return session;
    }

    /**
     * Reset a session to WAITING state.
     * It must be in SUCCESS or FAIL state, but WAITING is tolerated (no change inflicted)
     * @param sessionID ID of the session
     * @return the session
     */
    public Session reset(String sessionID) {
        Session session = getSessionNotNull(sessionID);
        if (session.getStatus() == Session.Status.SUCCESS || session.getStatus() == Session.Status.FAIL) {
            session.reset();
            iCache.put(sessionID, session);
        } else if (session.getStatus() != Session.Status.WAITING) {
            // waiting state is tolerated
            throw new IllegalArgumentException("4003");
        }
        return session;
    }
}
