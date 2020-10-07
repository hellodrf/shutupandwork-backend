package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.Session;
import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.SessionService;
import com.cervidae.shutupandwork.service.UserService;
import com.cervidae.shutupandwork.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("session")
public class SessionController {

    private final SessionService sessionService;

    private final UserService userService;

    @Autowired
    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping(params = {"sessionID"})
    public Response<Session> get(@RequestParam String sessionID) {
        Session session = sessionService.getSessionNotNull(sessionID);
        return Response.success(session);
    }

    @PostMapping(params = {"username", "sessionID", "join"})
    public Response<Session> join(@RequestParam String username, @RequestParam String sessionID) {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("cannot find user in database");
        }
        Session session = sessionService.join(user, sessionID);
        return Response.success(session);
    }

    @PostMapping(params = {"username", "sessionID", "leave"})
    public Response<?> leave(@RequestParam String username, @RequestParam String sessionID) {
        sessionService.leave(username, sessionID);
        return Response.success();
    }

    @PostMapping(params = {"sessionID", "target", "start"})
    public Response<Session> start(String sessionID, long target) {
        return Response.success(sessionService.start(sessionID, target));
    }

    @PostMapping(params = {"sessionID", "success"})
    public Response<Session> success(@RequestParam String sessionID) {
        return Response.success(sessionService.success(sessionID));
    }

    @PostMapping(params = {"username", "sessionID", "fail"})
    public Response<Session> fail(String sessionID, String username) {
        return Response.success(sessionService.fail(sessionID, username));
    }
}
