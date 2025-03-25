package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }
}
