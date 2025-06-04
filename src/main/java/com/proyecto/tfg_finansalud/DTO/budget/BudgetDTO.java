package com.proyecto.tfg_finansalud.DTO.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private String name;
    private Double budget;
    private Double budgetCount;
    private String color;


}
