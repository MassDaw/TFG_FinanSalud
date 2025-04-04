package com.proyecto.tfg_finansalud.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@Builder
@Getter
@Setter
public class Item {
    @Id
    private String id;
    private String ItemName;
    private String ItemDescription;
    private Double ItemPrice;
}
