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
@RequestMapping("/budget")
@Slf4j
public class RestBudgetController {

    private final UserService userService;
    private final BudgetMapper budgetMapper;
    private final BudgetService budgetService;


    @GetMapping("/getAll")
    public ResponseEntity<List<BudgetDTO>> budget() {
        try {
            return ResponseEntity.ok(
                    budgetMapper.toDTOList(userService.getBudget())
            );
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/new")
    public ResponseEntity<?> newBudget(@RequestBody BudgetDTO budgetDTO) {
        try {
            Budget budget = budgetMapper.toEntity(budgetDTO);
            budgetService.save(budget);
            userService.userNewBudget(budget);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("detele")
    public ResponseEntity<?> deleteBudget(@RequestBody String budgetName) {
        try{
            userService.userDeleteBudget(budgetName);

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
