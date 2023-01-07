package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.security.anotations.isAdmin;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.frontendcommunication.custom.AuthRequest;
import com.example.databasedemo2.frontendcommunication.custom.AuthResponse;
import com.example.databasedemo2.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody User newUser) {
        return authService.register(newUser, false);
    }

    @isAdmin
    @PostMapping("/register-admin")
    public AuthResponse registerAdmin(@Valid @RequestBody User newUser) {
        return authService.register(newUser, true);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
}
