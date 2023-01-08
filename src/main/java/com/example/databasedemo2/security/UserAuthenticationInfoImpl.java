package com.example.databasedemo2.security;

import com.example.databasedemo2.entitymanagement.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationInfoImpl implements UserAuthenticationInfo <User> {
    @Override
    public User getAuthenticationInfo() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
    }

    public boolean isAdmin() {
        return getAuthenticationInfo().getRole().getName().equals("ADMIN");
    }

    public boolean isClient() {
        return getAuthenticationInfo().getRole().getName().equals("CZYTELNIK");
    }
}
