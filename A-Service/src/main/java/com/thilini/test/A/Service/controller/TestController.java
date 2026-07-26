package com.thilini.test.A.Service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class TestController {

    @Value("${service.b.url}")
    private String serviceBUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/test")
    public String test() {
        return restTemplate.getForObject(
                serviceBUrl + "/api/message",
                String.class
        );
    }
}
