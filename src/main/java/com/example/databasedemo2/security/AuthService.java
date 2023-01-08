package com.example.databasedemo2.security;

import com.example.databasedemo2.entitymanagement.entities.Role;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.services.RoleService;
import com.example.databasedemo2.entitymanagement.services.UserService;
import com.example.databasedemo2.exceptions.custom.RegistrationException;
import com.example.databasedemo2.frontendcommunication.custom.AuthRequest;
import com.example.databasedemo2.frontendcommunication.custom.AuthResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(User newUser, boolean isAdmin) throws RegistrationException {
        if (userService.existsByEmail(newUser.getEmail()))
            throw new RegistrationException();

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if (!isAdmin || newUser.getRole() == null) {
            Role defaultRole = roleService.getDefaultRole();
            newUser.setRole(defaultRole);
        }

        newUser.setCreatedAt(new Date());
        newUser = userService.add(newUser);
        String token = jwtService.generateToken(newUser);
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest authRequest) throws BadCredentialsException, LockedException, DisabledException {
        // throws exception if login or password are invalid
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));

        User user = (User) userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}