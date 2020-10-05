package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.pojo.Session;
import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.util.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    final ICache<String, Session> iCache;

    @Autowired
    public SessionService(ICache<String, Session> iCache) {
        this.iCache = iCache;
    }

    public Session joinOrCreate(User user, String sessionID) {
        // validate sessionID
        if (!sessionID.matches(Constants.sessionIDRegex)) {
            throw new IllegalArgumentException("sessionID must be a 6 digit number");
        }
        Session session;
        if (iCache.contains(sessionID)) {
            session = iCache.select(sessionID);
            if (!session.addUser(user)) {
                throw new IllegalArgumentException("user already joined");
            }
        } else {
            session = new Session(user, sessionID);
            iCache.insert(sessionID, session);
        }
        return session;
    }

    public Session getSession(String sessionID) {
        return iCache.select(sessionID);
    }

    public void setStatus(String sessionID, Session.Status status) {
        if (iCache.contains(sessionID)) {
            Session session = iCache.select(sessionID);
            session.setStatus(status);
        } else {
            throw new IllegalArgumentException("cannot find specified session");
        }
    }
}
