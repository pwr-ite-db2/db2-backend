package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.anotations.isAdmin;
import com.example.databasedemo2.anotations.isLoggedIn;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.services.UserService;
import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;
import com.example.databasedemo2.frontendcommunication.custom.EditorPaneResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RolesAllowed({"AUTOR", "ADMIN"})
    @GetMapping("/author-working-pane")
    public List<ArticleInMakingView> getAuthorPane() {
        return userService.getArticlesInMakingByCurrentUser();
    }

    @RolesAllowed({"REDAKTOR", "ADMIN"})
    @GetMapping("/editor-working-pane")
    public EditorPaneResponse getEditorPane() {
        return userService.getEditorPane();
    }

    @isAdmin
    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @isLoggedIn
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
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