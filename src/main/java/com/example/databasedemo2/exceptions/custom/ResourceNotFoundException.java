package com.example.databasedemo2.exceptions.custom;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource not found!");
    }

    @Override
    public String getMessage() {
        return "Resource not found!";
    }
}