package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }
    public void saveAll(List<Budget> budgets) {
        budgetRepository.saveAll(budgets);
    }
}
