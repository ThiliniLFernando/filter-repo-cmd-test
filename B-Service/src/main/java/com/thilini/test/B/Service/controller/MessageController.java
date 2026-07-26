package com.thilini.test.B.Service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Value("${app.message}")
    private String message;

    @Value("${environment}")
    private String environment;

    @GetMapping("/message")
    public String message() {
        return message + " | Environment: " + environment;
    }

    @GetMapping("/health")
    public String health() {
        return "Application Healthy";
    }
}
