package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.user.UserDTO;
import com.proyecto.tfg_finansalud.DTO.user.UserMapper;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestMainController {

    final UserService userService;
    final UserMapper userMapper;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try{
            userService.save(userMapper.userDTOToUser(userDTO));
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
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
