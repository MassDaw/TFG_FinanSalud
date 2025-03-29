package com.proyecto.tfg_finansalud.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class Item {
    private String ItemName;
    private String ItemDescription;
    private Double ItemPrice;
}
