package com.proyecto.tfg_finansalud.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@Document(collection = "Items")
public class Item {
    @Id
    @JsonIgnore
    private String id;
    private String itemName;
    private String itemDescription;
    private Double itemPrice;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate itemDate;
    private Boolean Income; // true si es ingreso, false si es gasto
}
