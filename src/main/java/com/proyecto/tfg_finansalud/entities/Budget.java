package com.proyecto.tfg_finansalud.entities;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Getter
@Setter
@Document(collection = "User")
public class Budget {
    @Id
    private String id;
    private String name;
    private Double budget;
    private String color;
}
