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
import org.springframework.web.multipart.MultipartFile;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/user/update-email")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> payload) {
        try {
            String username = userService.getAuthenticatedUsername();
            String newEmail = payload.get("email");

            if (newEmail == null || newEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "El correo electrónico no puede estar vacío"));
            }

            userService.updateEmail(username, newEmail);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Correo electrónico actualizado correctamente",
                    "email", newEmail
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al actualizar el correo: " + e.getMessage()));
        }
    }

    @PostMapping("/user/upload-profile-pic")
    public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file) {
        try {
            String username = userService.getAuthenticatedUsername();
            String imageUrl = userService.saveProfileImage(file);
            userService.updateProfileImage(username, imageUrl);

            return ResponseEntity.ok(Map.of("success", true, "imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al subir la imagen: " + e.getMessage()));
        }
    }

    @GetMapping("/uploads/profile-pics/{imageURL}")
    public ResponseEntity<?> getImage(@PathVariable("imageURL") String URLfile) {
        try {
            MultipartFile filee = userService.getProfileImageAsMultipartFile(URLfile);

            return ResponseEntity.ok(Map.of("success", true, "image", filee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al cargar la imagen: " + e.getMessage()));
        }


        @RestController
        @RequestMapping("/user")
         class UserController {
            private final UserService userService;
            private final UserRepository userRepository;

            public UserController(UserService userService, UserRepository userRepository) {
                this.userService = userService;
                this.userRepository = userRepository;
            }

            @GetMapping("/me")
            public ResponseEntity<?> getCurrentUser() {
                try {
                    String username = userService.getAuthenticatedUsername();
                    Usuario user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    String profileImageUrl = user.getProfileImageUrl() != null ?
                            user.getProfileImageUrl() : "/uploads/profile-pics/profile-pic.jpg";

                    return ResponseEntity.ok(Map.of(
                            "username", username,
                            "email", user.getEmail(),
                            "profileImageUrl", profileImageUrl
                    ));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Error al obtener el usuario: " + e.getMessage()));
                }
            }

        }
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
