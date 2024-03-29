package com.example.databasedemo2.exceptions;

import com.example.databasedemo2.exceptions.custom.AuthorizationException;
import com.example.databasedemo2.exceptions.custom.RegistrationException;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFoundExceptions(ResourceNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public Map<String, String> handleLoggingExceptions(AuthenticationException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegistrationException.class)
    public Map<String, String> handleRegistrationExceptions(RegistrationException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleEntityNotFoundExceptions(EntityNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public Map<String, String> handleAuthorizationExceptions(AuthorizationException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLException.class)
    public Map<String, String> handleSQLExceptions(SQLException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeExceptions(RuntimeException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", e.getMessage());
        return errors;
    }
}