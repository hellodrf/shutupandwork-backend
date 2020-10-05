package com.cervidae.shutupandwork.service;

import com.cervidae.shutupandwork.pojo.Session;
import com.cervidae.shutupandwork.pojo.User;
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
        Session session;
        if (iCache.contains(sessionID)) {
            session = iCache.select(sessionID);
            if (!session.addUser(user)) {
                throw new IllegalArgumentException("User already joined.");
            }
        } else {
            session = new Session(user, sessionID);
        }
        return session;
    }
}
