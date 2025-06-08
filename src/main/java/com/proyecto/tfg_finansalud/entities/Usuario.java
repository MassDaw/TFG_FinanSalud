package com.proyecto.tfg_finansalud.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class Usuario {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private String profileImageUrl; // Asegúrate de que este atributo exista


    @Builder.Default
    Set<Profile> profiles = new HashSet<>();
    @Builder.Default
    @DBRef(lazy = false) // DBRef  para que solo referencie ID /// (lazy = false) para que cargue TODOS los datos al llamar usuario
    List<Budget> budgets = new ArrayList<>();
    @DBRef
    @Builder.Default
    private List<Income> income = new ArrayList<>();
    private boolean emailVerified;

    @DBRef(lazy = false)
    private List<Token> tokens = new ArrayList<>();

}
