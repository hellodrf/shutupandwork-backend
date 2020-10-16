package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.UserService;
import com.cervidae.shutupandwork.util.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


/**
 * @author AaronDu
 */
@RestController
@RequestMapping("users")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get a user by username.
     * @param username target's username
     * @return required user
     */
    @GetMapping(params = {"username"})
    public Response<User> get(@RequestParam String username) {
        User user = userService.getByUsernameNotNull(username);
        return Response.success(user);
    }

    @GetMapping
    public Response<User> get() {
        Subject subject = SecurityUtils.getSubject();
        User user = userService.getByUsernameNotNull((String) subject.getPrincipal());
        return Response.success(user);
    }

    /**
     * Update a user's score.
     * @param newScore target's score
     * @return required user
     */
    @PostMapping(params = {"newScore"})
    public Response<?> updateScore(@RequestParam int newScore) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        userService.updateScore(username, newScore);
        return Response.success(userService.getByUsernameNotNull(username));
    }

    //@PostMapping(params = {"newUsername"})
    public Response<?> updateUsername(@RequestParam String newUsername) {
        Subject subject = SecurityUtils.getSubject();
        String oldUsername = (String) subject.getPrincipal();
        User user = userService.getByUsernameNotNull(oldUsername);
        userService.updateUsername(user.getId(), oldUsername, newUsername);
        logout();
        return Response.success(userService.getByUsernameNotNull(newUsername));
    }

    @PostMapping(value = "login", params = {"u", "p"})
    public Response<User> login(@RequestParam String u, @RequestParam String p) {
        Assert.hasText(u, "3006");
        Assert.hasText(p, "3006");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(u, p);
        try{
            subject.login(token);
        } catch (AuthenticationException e){
            return Response.fail("3004");
        }
        if (!subject.isAuthenticated()) return Response.fail("3004");
        else return Response.success(userService.getByUsername(u));
    }

    @PostMapping(value = "logout")
    public Response<?> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if (subject.isAuthenticated()) return Response.fail();
        else return Response.success();
    }

    @PostMapping(value = "register", params = {"u", "p"})
    public Response<User> register(@RequestParam String u, @RequestParam String p) {
        Assert.hasText(u, "3006");
        Assert.hasText(p, "3006");
        User user = userService.register(u, p);
        Assert.notNull(user, "3002");
        return Response.success(user);
    }

    @GetMapping(value = "403")
    public void unauthorized() {
        throw new UnauthenticatedException();
    }
}
