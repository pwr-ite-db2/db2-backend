package com.example.databasedemo2.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserAuthenticationInfo <T extends UserDetails> {
    T getAuthenticationInfo();
}
