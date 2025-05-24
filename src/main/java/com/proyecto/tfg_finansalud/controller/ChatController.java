// src/main/java/com/proyecto/tfg_finansalud/controller/ChatController.java
package com.proyecto.tfg_finansalud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChatController {

    @Value("${python.api.url}")
    private String pythonApiUrl;

    private final RestTemplate restTemplate;

    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/chat/finanzas")
    public ResponseEntity<String> getRecomendacionFinanciera() {
        String response = restTemplate.getForObject(pythonApiUrl + "/api/chat/finanzas", String.class);
        return ResponseEntity.ok(response);
    }
}