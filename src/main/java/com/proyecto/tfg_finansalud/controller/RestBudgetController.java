package com.proyecto.tfg_finansalud.controller;


import com.proyecto.tfg_finansalud.DTO.budget.BudgetDTO;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/get")
@Slf4j
public class RestBudgetController {

    private final UserService userService;
    private final BudgetMapper budgetMapper;


    @GetMapping("/budget")
    public ResponseEntity<List<BudgetDTO>> budget() {
        try {
            System.out.println(budgetMapper.toDTO(userService.getBudget()));
            return ResponseEntity.ok(
                    budgetMapper.toDTO(userService.getBudget())
            );
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
