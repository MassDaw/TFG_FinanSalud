package com.proyecto.tfg_finansalud.services;


import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveAll(List<Budget> budgets) {
        budgetRepository.saveAll(budgets);
    }

    // todo budget que se cree, tendra como fecha el mes y año en curso
    public Budget save(Budget budget) {
        // Establecer fecha actual
        budget.setYearMonth(YearMonth.now().atDay(1));
        budget.setMonth(YearMonth.now().getMonthValue());

        // Inicializar campos si es nuevo
        if (budget.getId() == null) {
            budget.setItems(new ArrayList<>());
            budget.setBudgetCount(0.0);
            budget.setNotiPorcentaje(80); // valor por defecto
        }

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


    public Map<String, List<Item>> getItemfromBudget(List<String> budgets) {
        List<Budget> budgetList = budgetRepository.findAllByIdIn(budgets);
        //crea un mapa donde la clave es la categoria y el valor una lista de gastos/items

        return budgetList.stream().collect(Collectors.toMap(Budget::getName, Budget::getItems));
    }

    public Budget getBudget(String id) {
        Optional<Budget> budget = budgetRepository.findById(id);
        return budget.orElse(null);
    }

    public void addItemtoBudget(String budgetId, Item item) {
        Query query = new Query(Criteria.where("id").is(budgetId));
        // Agrega el item y suma el precio al budgetCount
        Update update = new Update()
                .push("items", item)
                .inc("budgetCount", item.getItemPrice());
        mongoTemplate.updateFirst(query, update, Budget.class);
    }

}
