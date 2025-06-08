package com.proyecto.tfg_finansalud.DTO.jwt;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginRequest {
    String username;
    String password;
}
