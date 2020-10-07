package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.pojo.User;
import com.cervidae.shutupandwork.service.UserService;
import com.cervidae.shutupandwork.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Update a user's data. If user do not exist, add it to the database.
     * Idempotent action.
     * Usage: POST /users?username=your_user_name?score=your_score
     * @param username target's username
     * @param score target's score
     * @return required user
     */
    @PostMapping(params = {"username", "score"})
    public Response<?> put(@RequestParam String username, @RequestParam int score) {
        if (userService.getByUsername(username) == null) {
            userService.add(username, score);
        }
        else {
            userService.update(username, score);
        }
        return Response.success();
    }
}
