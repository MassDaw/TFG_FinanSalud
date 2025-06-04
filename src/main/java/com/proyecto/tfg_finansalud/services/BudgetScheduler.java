package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.BudgetRepository;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BudgetScheduler {

    private final UserRepository usuarioRepository;
    private final BudgetRepository budgetRepository;

    @Scheduled(cron = "0 5 0 1 * ?")
    public void cloneMonthlyBudgets() {
        cloneBudgetsForNewMonthInBatches();
    }
    public void cloneBudgetsForNewMonthInBatches() {
        LocalDate now = LocalDate.now();
        LocalDate newYearMonth = LocalDate.of(now.getYear(), now.getMonth(), 1);

        int page = 0;
        int pageSize = 100;
        Page<Usuario> usuarioPage;

        do {
            usuarioPage = usuarioRepository.findAll(PageRequest.of(page, pageSize));
            for (Usuario usuario : usuarioPage.getContent()) {
                List<Budget> nuevosBudgets = usuario.getBudgets().stream()
                        .map(original -> Budget.builder()
                                .name(original.getName())
                                .description(original.getDescription())
                                .budget(original.getBudget())
                                .budgetCount(0.0)
                                .color(original.getColor())
                                .month(newYearMonth.getMonthValue())
                                .yearMonth(newYearMonth)
                                .notiPorcentaje(original.getNotiPorcentaje())
                                .items(List.of()) // vacíos o clonados si quieres
                                .build())
                        .map(budgetRepository::save)
                        .collect(Collectors.toList());

                usuario.getBudgets().addAll(nuevosBudgets);
                usuarioRepository.save(usuario);
                System.out.println(usuario);
            }

            page++;
        } while (!usuarioPage.isLast());
    }


}
