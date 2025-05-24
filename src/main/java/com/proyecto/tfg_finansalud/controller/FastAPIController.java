package com.proyecto.tfg_finansalud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/dashboard")
public class FastAPIController {

    @Value("${python.api.url}")
    private String pythonApiUrl;

    private final RestTemplate restTemplate;

    public FastAPIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/crypto")
    public ResponseEntity<String> getCryptoMarketData() {
        // Llamar a la API de Python y devolver los resultados
        String response = restTemplate.getForObject(pythonApiUrl + "/api/crypto-market", String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stocks")
    public ResponseEntity<String> getStockMarketData() {
        // Llamar a la API de Python y devolver los resultados
        String response = restTemplate.getForObject(pythonApiUrl + "/api/stock-market", String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorites")
    public ResponseEntity<String> getFavoriteAssets() {
        // Llamar a la API de Python para obtener activos favoritos
        String response = restTemplate.getForObject(pythonApiUrl + "/api/favorites", String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchAssets(String query) {
        // Llamar a la API de Python para buscar activos
        String response = restTemplate.getForObject(
                pythonApiUrl + "/api/search?query={query}",
                String.class,
                query
        );
        return ResponseEntity.ok(response);
    }

}
