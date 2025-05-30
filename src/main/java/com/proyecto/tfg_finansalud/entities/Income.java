package com.proyecto.tfg_finansalud.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
@Document(collection = "Income")
public class Income {
    @Id
    private String id;
    private Double incomes;
    private Integer month;


    @DBRef(lazy = false)
    private List<Item> items = new ArrayList<>();
}
