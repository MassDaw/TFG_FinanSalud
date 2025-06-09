package com.proyecto.tfg_finansalud.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.MediaTypeCache;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotConnection {

    private final WebClient webClient;

    public Mono<String> sendToApi(Double ingresos, Double total, Map<String, Double> gastos, String url) {
        Map<String, Object> datosFinancieros = new HashMap<>();

        datosFinancieros.put("ingresos", ingresos);//ingresos
        datosFinancieros.put("gastos_totales", total);//total
        datosFinancieros.put("gastos_por_categoria", gastos);

        return webClient.post()
                .uri( url + "/api/chat/finanzas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(datosFinancieros)
                .retrieve()
                .bodyToMono(String.class);
    }
}

