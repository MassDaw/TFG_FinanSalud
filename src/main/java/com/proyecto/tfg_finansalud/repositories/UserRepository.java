package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<Usuario, String> {


    @Query("{'username': ?#{#authentication.principal.username}}")
    Optional<Usuario> findUser();


    @Query("{ 'username': ?0, '$push': { 'budgets': ?1 } }")
    void addBudget(String username, Budget budget);

    Optional<Usuario> findByUsername(String username);
}
