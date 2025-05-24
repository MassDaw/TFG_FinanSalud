package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Income;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.repositories.IncomeRepository;
import com.proyecto.tfg_finansalud.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final ItemRepository itemRepository;

    public Income save(Income income) {
        return incomeRepository.save(income);
    }
    public Income createIncome(){
        return Income.builder().build();
    }

    public Income addItemToIncome(String incomeId, Item newItem) {
        // 1. Buscar Income por ID
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income no encontrado"));

        // 2. Guardar el nuevo Item
        Item savedItem = itemRepository.save(newItem);

        // 3. Agregar el Item al Income
        income.getItems().add(savedItem);

        // 4. Guardar Income actualizado
        return incomeRepository.save(income);
    }

}
