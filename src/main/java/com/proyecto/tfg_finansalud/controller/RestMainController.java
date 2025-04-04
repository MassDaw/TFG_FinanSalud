package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.user.UserDTO;
import com.proyecto.tfg_finansalud.DTO.user.UserMapper;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class RestMainController {

    final UserService userService;
    final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());

        try {
            userService.save(userMapper.userDTOToUser(userDTO));

            // Devuelve JSON correcto
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Registro exitoso"));
        } catch (Exception e) {
            log.error("Error al registrar usuario", e);

            // Devuelve JSON incluso si hay errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al registrar usuario: " + e.getMessage()));
        }

    }


//    @GetMapping("/allBudget")
//    public ResponseEntity<?> getBudget() {
//
//
//        return ResponseEntity.status(HttpStatus.CREATED);
//    }

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
