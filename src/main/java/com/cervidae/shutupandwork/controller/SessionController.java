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

    @PostMapping(params = {"username", "sessionID"})
    public Response<Session> join(@RequestParam String username, @RequestParam String sessionID) {
        User user = userService.getByUsername(username);
        if (user==null) {
            throw new IllegalArgumentException("cannot find user in database");
        }
        Session session = sessionService.joinOrCreate(user, sessionID);
        return Response.success(session);
    }

    @GetMapping(params = {"sessionID"})
    public Response<Session> getSession(@RequestParam String sessionID) {
        Session session = sessionService.getSession(sessionID);
        return Response.success(session);
    }

    @PostMapping(params = {"sessionID", "status"})
    public Response<?> setStatus(Session.Status status) {
        return sessionService.setStatus(status)? Response.success() : Response.fail();
    }
}
