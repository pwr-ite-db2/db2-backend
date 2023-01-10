package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.services.UserService;
import com.example.databasedemo2.security.anotations.isAdmin;
import com.example.databasedemo2.security.anotations.isEmployee;
import com.example.databasedemo2.security.anotations.isLoggedIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @isEmployee
    @GetMapping("/current/working-pane")
    public List<?> getWorkingPane() {
        return userService.getWorkingPane();
    }

    @isAdmin
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) Map<String, String> params) {
        return userService.getAll(params);
    }

    @isLoggedIn
    @PutMapping("/current")
    public User updateCurrentUserInfo(@Valid @RequestBody User user, @RequestParam(required = false) Map<String, String> params) {
        return userService.updateCurrentUserInfo(user, params);
    }

    @isAdmin
    @PutMapping
    public User updateUser(@Valid @RequestBody User user, @RequestParam(required = false) Map<String, String> params) {
        return userService.update(user, params, true);
    }

    @isLoggedIn
    @GetMapping("/current")
    public User getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @isAdmin
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int userId) {
        return userService.getById(userId);
    }

    @isAdmin
    @DeleteMapping("/{id}")
    public boolean deleteUserById(@PathVariable("id") int userId) {
        return userService.deleteById(userId);
    }
}