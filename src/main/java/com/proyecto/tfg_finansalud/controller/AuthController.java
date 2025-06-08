package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.jwt.LoginRequest;
import com.proyecto.tfg_finansalud.DTO.jwt.RegisterRequest;
import com.proyecto.tfg_finansalud.DTO.jwt.TokenResponse;
import com.proyecto.tfg_finansalud.services.AuthService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return ResponseEntity.ok("Email verificado correctamente");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o expirado");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody final RegisterRequest registerRequest) throws Exception {
        try{
            final TokenResponse token = authService.register(registerRequest);
            return ResponseEntity.ok(token);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest loginRequest) throws Exception {
        try{
            final TokenResponse token = authService.login(loginRequest);
            return ResponseEntity.ok(token);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

    }
}