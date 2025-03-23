package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.user.UserDTO;
import com.proyecto.tfg_finansalud.entities.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestMainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody UserDTO user) {
//        Usuario foundUser = userRepository.findByUsername(user.getUsername())
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//
//        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
//            return ResponseEntity.ok("Login exitoso");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
//    }
}
