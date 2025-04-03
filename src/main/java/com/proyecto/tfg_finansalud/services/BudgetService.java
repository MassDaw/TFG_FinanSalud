package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;


    public void saveAll(List<Budget> budgets) {
        budgetRepository.saveAll(budgets);
    }

    public Budget save(Budget budget) {
        budget.setYearMonth(YearMonth.now().atDay(1));
        return budgetRepository.save(budget);
    }

    public void removeID(String id) {
        budgetRepository.deleteById(id);
    }


}
