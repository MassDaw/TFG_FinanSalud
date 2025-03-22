package com.proyecto.tfg_finansalud.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
@Getter
@Setter
@Document(collection = "Profile")
public class Profile {
    private long id;
    private String name;
    private Set<Usuario> users;
}
