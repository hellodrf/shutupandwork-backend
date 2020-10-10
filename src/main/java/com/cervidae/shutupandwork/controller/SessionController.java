package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Session;
import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.SessionService;
import com.cervidae.shutupandwork.service.UserService;
import com.cervidae.shutupandwork.util.Constants;
import com.cervidae.shutupandwork.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @author AaronDu
 */
@RestController
@RequestMapping("sessions")
public class SessionController {

    private final SessionService sessionService;

    private final UserService userService;

    @Autowired
    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     * Get a specific session with sessionID, not nullable (will throw exception)
     * Usage: GET /sessions?sessionID=your_sessionID
     * @param sessionID ID of the session
     * @return the session
     */
    @GetMapping(params = {"sessionID"})
    public Response<Session> get(@RequestParam String sessionID) {
        Session session = sessionService.getSessionNotNull(sessionID);
        return Response.success(session);
    }

    /**
     * Join the specified session. If not exist, create a session
     * Usage: POST /sessions?username=your_username?sessionID=your_sessionID?join
     * @param username the user's username
     * @param sessionID ID of the session
     * @return the session
     */
    @PostMapping(params = {"username", "sessionID", "join"})
    public Response<Session> join(@RequestParam String username, @RequestParam String sessionID) {
        User user = userService.getByUsernameNotNull(username);
        Session session = sessionService.join(user, sessionID);
        return Response.success(session);
    }

    /**
     * Leave the specified session. Will fail the session if session is in active status
     * Usage: POST /sessions?username=your_username?sessionID=your_sessionID?leave
     * @param username the user's username
     * @param sessionID ID of the session
     */
    @PostMapping(params = {"username", "sessionID", "leave"})
    public Response<?> leave(@RequestParam String username, @RequestParam String sessionID) {
        User user = userService.getByUsernameNotNull(username);
        sessionService.leave(user, sessionID);
        return Response.success();
    }

    /**
     * Start the session
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * Usage: POST /sessions?sessionID=your_sessionID?target=your_target?start
     * @param sessionID ID of the session
     * @param target the target of the session
     * @return the session
     */
    @PostMapping(params = {"sessionID", "target", "start"})
    public Response<Session> start(String sessionID, long target) {
        return Response.success(sessionService.start(sessionID, target));
    }

    /**
     * Mark the session as succeed
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * Usage: POST /sessions?sessionID=your_sessionID?success
     * @param sessionID ID of the session
     * @return the session
     */
    @PostMapping(params = {"sessionID", "success"})
    public Response<Session> success(@RequestParam String sessionID) {
        return Response.success(sessionService.success(sessionID));
    }

    /**
     * Mark the session as failed
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * Usage: POST /sessions?sessionID=your_sessionID?username=your_username?fail
     * @param sessionID ID of the session
     * @param username user to blame
     * @return the session
     */
    @PostMapping(params = {"username", "sessionID", "fail"})
    public Response<Session> fail(String sessionID, String username) {
        return Response.success(sessionService.fail(sessionID, username));
    }

    /**
     * Reset a session to WAITING state.
     * It must be in SUCCESS or FAIL state, but WAITING is tolerated (no change inflicted)
     * Usage: POST /sessions?sessionID=your_sessionID?reset
     * Pessimistic lock: since this function need only be called EXACTLY ONCE
     * @param sessionID ID of the session
     * @return the session
     */
    @PostMapping(params = {"sessionID", "reset"})
    public Response<Session> reset(String sessionID) {
        return Response.success(sessionService.reset(sessionID));
    }

    /**
     * Remove expired sessions (see Constants.SESSION_EXPIRY, currently 1 day)
     * There is a cron task scheduled 3am everyday, but you can call it manually anytime
     * @param password admin password
     * @return success response
     */
    @PostMapping(params = {"gc", "password"})
    public Response<?> gc(String password) {
        Assert.isTrue(password.equals(Constants.ADMIN_PASSWORD), "1001");
        sessionService.collectExpiredSessions();
        return Response.success("Garbage collect completed.");
    }
}
