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
    Budget toEntity(BudgetDTO budgetDTO);
    BudgetDTO toDTO(Budget budget);
    List<Budget> toEntityList(List<BudgetDTO> budgetDTOList);
    List<BudgetDTO> toDTOList(List<Budget> budgetList);

    BudgetDTO2 toDTO2 (Budget budget);
    List<BudgetDTO2> toDTOList2 (List<Budget> budgetList);
}
