package com.example.databasedemo2.exceptions.custom;

public class RegistrationException extends RuntimeException {
    public RegistrationException() {
        super("Login already taken!");
    }

    @Override
    public String getMessage() {
        return "Login already taken!";
    }
}
