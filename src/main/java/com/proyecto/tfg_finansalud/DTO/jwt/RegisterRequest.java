package com.proyecto.tfg_finansalud.DTO.jwt;

import lombok.Data;
import lombok.Getter;

@Getter
public class RegisterRequest {
    String username;
    String email;
    String password;
}
