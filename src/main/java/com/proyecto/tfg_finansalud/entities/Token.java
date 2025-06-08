package com.proyecto.tfg_finansalud.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Token {
    public enum TokenType {
        BEARER
    }
    @Id
    private String id;
    public String token;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;

    @DBRef(lazy = false)
    private Usuario usuario;
}
