package com.example.databasedemo2.security;

import com.example.databasedemo2.entitymanagement.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationInfoImpl implements UserAuthenticationInfo <User> {
    @Override
    public User getAuthenticationInfo() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
