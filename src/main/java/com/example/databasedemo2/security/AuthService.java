package com.example.databasedemo2.security;

import com.example.databasedemo2.businesslogic.services.UserService;
import com.example.databasedemo2.dataaccess.entities.Role;
import com.example.databasedemo2.dataaccess.entities.User;
import com.example.databasedemo2.frontendcommunication.custom.AuthRequest;
import com.example.databasedemo2.frontendcommunication.custom.AuthResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.builder().id(1).build());
        newUser.setCreatedAt(new Date());
        newUser = userService.addOrUpdate(newUser);
        Map<String, Object> idMapping = new HashMap<>();
        idMapping.put("id", newUser.getId());
        String token = jwtService.generateToken(idMapping, newUser);
        return new AuthResponse(token);
    }

    @Transactional
    public AuthResponse authenticate(AuthRequest authRequest) {
        // throws exception if login or password are invalid
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));

        User user = (User) userService.loadUserByUsername(authRequest.getEmail());
        Map<String, Object> idMapping = new HashMap<>();
        idMapping.put("id", user.getId());
        String token = jwtService.generateToken(idMapping, user);
        return new AuthResponse(token);
    }
}