package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.services.ChatBotConnection;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatBotConnection chatService;
    private final UserService userService;

    @Value("${python.api.url}")
    private String pythonApiUrl;


    @GetMapping("/chat/finanzas")
    public ResponseEntity<String> getRecomendacionFinanciera() {
        List<Budget> listaBudget = userService.getBudget();
        double total = listaBudget.stream()
                .mapToDouble(Budget::getBudgetCount)
                .sum();
        Map<String, Double> map = listaBudget.stream().collect(Collectors.toMap(Budget::getName, Budget::getBudgetCount));

        String response = chatService.sendToApi(userService.getIncome(), total, map).block();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}