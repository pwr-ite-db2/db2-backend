package com.example.databasedemo2.frontendcommunication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/healthcheck")
public class ServerHealthcheck {
    @GetMapping
    public String healthcheck() {
        return "Server is running, date: " + new Date(System.currentTimeMillis());
    }
}