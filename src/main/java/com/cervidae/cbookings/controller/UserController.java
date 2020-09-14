package com.cervidae.cbookings.controller;

import com.cervidae.cbookings.pojo.Role;
import com.cervidae.cbookings.pojo.User;
import com.cervidae.cbookings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="user}")
    public User getByUsername(String username) {
        return userService.getByUsername(username);
    }

    @PostMapping(value="user/add/{username}&{password}&{name}&{address}&{phone}&{email}&{extra}&{roleId}")
    public boolean add(@PathVariable String username, @PathVariable String password, @PathVariable String name,
                       @PathVariable String address, @PathVariable String phone, @PathVariable String email,
                       @PathVariable String extra, @PathVariable int roleId) {
        if (getByUsername(username)==null) {
            userService.add(username, password, name, address, phone, email, extra, roleId);
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping(value = "role/{username}")
    public Set<String> getRoleByUsername(@PathVariable String username) {
        return userService.getRoles(username);
    }
}
