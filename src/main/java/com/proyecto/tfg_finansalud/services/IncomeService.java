package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Income;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.repositories.IncomeRepository;
import com.proyecto.tfg_finansalud.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;


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
    public Income createIncome(String income){
        return save(Income.builder().name(income).date(LocalDate.now().withDayOfMonth(1)).build());
    }

    public Income addItemToIncome(String incomeId, Item newItem) {
        // 1. Buscar Income por ID
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income no encontrado"));

        income.getItems().add(newItem);
        income.setIncomes(income.getIncomes() + newItem.getItemPrice());
        // 4. Guardar Income actualizado
        return incomeRepository.save(income);
    }


//    public String getCurrentUserId() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            return ((UserDetails) principal)CustomUserDetails .getId(); // ✅ ID del usuario
//        }
//
//        return null; // o lanzar excepción si no hay usuario autenticado
//    }

}
