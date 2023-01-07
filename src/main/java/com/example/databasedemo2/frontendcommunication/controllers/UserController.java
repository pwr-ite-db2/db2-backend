package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.services.UserService;
import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;
import com.example.databasedemo2.frontendcommunication.custom.EditorPaneResponse;
import com.example.databasedemo2.security.anotations.isAdmin;
import com.example.databasedemo2.security.anotations.isAuthor;
import com.example.databasedemo2.security.anotations.isEditor;
import com.example.databasedemo2.security.anotations.isLoggedIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @isAuthor
    @GetMapping("/author-working-pane")
    public List<ArticleInMakingView> getAuthorPane() {
        return userService.getAuthorPane();
    }

    @isEditor
    @GetMapping("/editor-working-pane")
    public EditorPaneResponse getEditorPane() {
        return userService.getEditorPane();
    }

    @isAdmin
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) Map<String, String> params) {
        return userService.getAll(params);
    }

    @isLoggedIn
    @PutMapping
    public User updateUser(@RequestBody User user, @RequestParam(required = false) Map<String, String> params) {
        return userService.update(user, params);
    }

    @isLoggedIn
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