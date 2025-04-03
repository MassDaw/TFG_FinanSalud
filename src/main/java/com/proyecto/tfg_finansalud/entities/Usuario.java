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

    Set<Profile> profiles = new HashSet<>();
    @DBRef
    List<Budget> budgets = new ArrayList<>();

}
