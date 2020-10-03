package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="user", params = {"username"})
    public User getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @PostMapping(value="user", params = {"username", "score"})
    public boolean update(@RequestParam String username, @RequestParam String score) {
        if (getByUsername(username)==null) {
            return userService.add(username, score);
        }
        else {
            return false;
        }
    }
}
