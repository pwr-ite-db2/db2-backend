package com.example.databasedemo2.exceptions.custom;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("You are not authorized to perform this action!");
    }

    @Override
    public String getMessage() {
        return "You are not authorized to perform this action!";
    }
}
