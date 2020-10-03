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

    /**
     * Get a user by username.
     * @param username target's username
     * @return required user
     */
    @GetMapping(value="user", params = {"username"})
    public User getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    /**
     * Update a user's data. If user do not exist, add it to the database.
     * @param username target's username
     * @param score target's score
     * @return required user
     */
    @PostMapping(value="user", params = {"username", "score"})
    public boolean update(@RequestParam String username, @RequestParam int score) {
        if (getByUsername(username)==null) {
            return userService.add(username, score);
        }
        else {
            return userService.update(username, score);
        }
    }
}
