package com.proyecto.tfg_finansalud.DTO.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String ItemName;
    private String ItemDescription;
    private Double ItemPrice;
}
