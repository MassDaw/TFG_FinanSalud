package com.proyecto.tfg_finansalud.entities;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@Document(collection = "Budget")
public class Budget {
    @Id
    private String id;
    private String name;
    private String description; //pc
    private Double budget;
    private Double budgetCount;
    private String color;
    private Integer month;
    private LocalDate yearMonth;

    private Integer notiPorcentaje;
    @DBRef(lazy = false)
    private List<Item> items = new ArrayList<>();

}
