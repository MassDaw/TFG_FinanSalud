package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;


    public void saveAll(List<Budget> budgets) {
        budgetRepository.saveAll(budgets);
    }

    // todo budget que se cree, tendra como fecha el mes y año en curso
    public Budget save(Budget budget) {
        budget.setYearMonth(YearMonth.now().atDay(1));
        return budgetRepository.save(budget);
    }

    public void removeID(String id) {
        budgetRepository.deleteById(id);
    }

    public void updateBudget(String id, Double newBudget) {
            Optional<Budget> budget = budgetRepository.findById(id);
            System.out.println(budget);
            if (budget.isPresent()) {
                budget.get().setBudget(newBudget);
                budgetRepository.save(budget.get());
            }
            else {
                log.info("No existe el budget con id " + id);
            }
    }


}
