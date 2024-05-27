package com.example.donation.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.29.215:3000"})
public class Main {

    @GetMapping("/")
    public String home() {
        return "Welcome to the home page of donation application.";
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, My Friend!";
    }

    @GetMapping("*")
    @PostMapping("*")
    public String error() {
        return "ye to error page hai 404";
    }
}
