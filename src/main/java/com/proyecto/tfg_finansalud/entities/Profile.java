package com.proyecto.tfg_finansalud.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Profile")
public class Profile {
    @Id
    private String id;
    private String name;
    private Set<Usuario> user;
}
