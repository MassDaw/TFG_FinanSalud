package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByToken(String token);
}
