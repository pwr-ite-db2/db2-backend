package com.example.databasedemo2.anotations;

import jakarta.annotation.security.PermitAll;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PermitAll
public @interface isLoggedIn {
}
