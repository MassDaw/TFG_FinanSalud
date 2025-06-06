package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return ResponseEntity.ok("Email verificado correctamente");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o expirado");
        }
    }
}