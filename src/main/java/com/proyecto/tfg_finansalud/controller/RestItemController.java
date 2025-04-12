package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
@Slf4j
public class RestItemController {
    private final UserService userService;
    private final BudgetMapper budgetMapper;
    private final BudgetService budgetService;

    @GetMapping("/currentMonth")
    public ResponseEntity<Map<String, List<Item>>> getItemsCurrentMonth() {
        try {
            List<String> budgetsID = userService.getBudgetID();//id de Budget del usuario en el mes actual

            return ResponseEntity.ok(
                    budgetService.getItemfromBudget(budgetsID));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
