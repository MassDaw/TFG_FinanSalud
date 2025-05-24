package com.proyecto.tfg_finansalud.controller;


import com.proyecto.tfg_finansalud.DTO.Item.ItemDTO;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.IncomeService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/income")
@Slf4j
public class RestIncomeController {
    private final UserService userService;
    private final IncomeService incomeService;

    @PostMapping("/newItem")
    public void newItem(@RequestBody ItemDTO itemDTO) {


    }

}
