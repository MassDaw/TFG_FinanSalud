package com.proyecto.tfg_finansalud.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Date itemDate;
}
