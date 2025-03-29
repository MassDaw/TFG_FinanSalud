package com.proyecto.tfg_finansalud.DTO.budget;

import com.proyecto.tfg_finansalud.entities.Budget;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface BudgetMapper {
    Budget toDTO(BudgetDTO budgetDTO);
    BudgetDTO toEntity(Budget budget);
    List<Budget> toEntity(List<BudgetDTO> budgetDTOList);
    List<BudgetDTO> toDTO(List<Budget> budgetList);
}
