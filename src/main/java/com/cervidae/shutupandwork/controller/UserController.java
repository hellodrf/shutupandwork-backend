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
     * Get current logged-in user
     * @return the user
     */
    @GetMapping
    public Response<User> get() {
        User user = userService.getByUsernameNotNull(userService.getCurrentUsername());
        return Response.success(user);
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

    /**
     * Update a user's score.
     * @param newScore target's score
     * @return required user
     */
    @PostMapping(params = {"newScore"})
    public Response<?> updateScore(@RequestParam int newScore) {
        String username = userService.getCurrentUsername();
        userService.updateScore(username, newScore);
        return Response.success(userService.getByUsernameNotNull(username));
    }

    /**
     * Login with credentials
     * @param u username
     * @param p password
     * @return the user
     */
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

    /**
     * Logout
     * @return a empty success response
     */
    @PostMapping(value = "logout")
    public Response<?> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if (subject.isAuthenticated()) return Response.fail("3007");
        else return Response.success();
    }

    /**
     * Register with credentials, will auto-login
     * @param u username
     * @param p password
     * @return the user
     */
    @PostMapping(value = "register", params = {"u", "p"})
    public Response<User> register(@RequestParam String u, @RequestParam String p) {
        Assert.hasText(u, "3006");
        Assert.hasText(p, "3006");
        User user = userService.register(u, p);
        Assert.notNull(user, "3002");
        return login(u, p);
    }

    /**
     * Redirect 403 Unauthorized errors
     */
    @GetMapping(value = "403")
    public void unauthorized() {
        throw new UnauthenticatedException();
    }

    /**
     * DEPRECATED! API CLOSED!
     * Update a user's username
     * @param newUsername new username
     * @return the user
     */
    @Deprecated
    //@PostMapping(params = {"newUsername"})
    public Response<?> updateUsername(@RequestParam String newUsername) {
        String oldUsername = userService.getCurrentUsername();
        User user = userService.getByUsernameNotNull(oldUsername);
        userService.updateUsername(user.getId(), oldUsername, newUsername);
        logout();
        return Response.success(userService.getByUsernameNotNull(newUsername));
    }
}
