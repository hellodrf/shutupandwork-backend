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
     * Get a user by username. (idempotent)
     * Usage: GET /users?username=your_user_name
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
     * Update a user's data. If user do not exist, add it to the database.
     * Idempotent action.
     * Usage: POST /users?username=your_username?score=your_score
     * @param username target's username
     * @param score target's score
     * @return required user
     */
    @PostMapping(params = {"username", "score"})
    public Response<?> update(@RequestParam String username, @RequestParam int score) {
        Subject subject = SecurityUtils.getSubject();
        User user = userService.getByUsernameNotNull((String) subject.getPrincipal());
        userService.update(user.getId(), username, score);
        return Response.success(userService.getByUsernameNotNull(username));
    }

    @PostMapping(value = "login", params = {"u", "p"})
    public Response<User> login(@RequestParam String u, @RequestParam String p){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(u, p);
        try{
            subject.login(token);
        } catch (AuthenticationException e){
            return Response.fail();
        }
        if (!subject.isAuthenticated()) return Response.fail();
        else return Response.success(userService.getByUsername(u));
    }

    @PostMapping(value = "logout")
    public Response<?> logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if (subject.isAuthenticated()) return Response.fail();
        else return Response.success();
    }

    @PostMapping(value = "register", params = {"u", "p"})
    public Response<User> register(@RequestParam String u, @RequestParam String p) {
        User user = userService.register(u, p);
        Assert.notNull(user, "3002");
        return Response.success(user);
    }

    @GetMapping(value = "403")
    public void unauthorized() {
        throw new UnauthenticatedException();
    }
}
