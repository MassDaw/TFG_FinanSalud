package com.proyecto.tfg_finansalud.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "Profile")
public class Profile {
    private long id;
    private String name;
    private Set<User> users;
}
