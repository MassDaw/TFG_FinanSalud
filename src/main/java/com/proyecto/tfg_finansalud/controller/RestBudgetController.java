package com.proyecto.tfg_finansalud.controller;


import com.proyecto.tfg_finansalud.DTO.budget.BudgetDTO;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.Update;
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

    //devuelve todos los budget del usuario que coincidan con el mes actual
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBudget(@RequestBody BudgetDTO budgetDTO) {
        try{
             String id = userService.returnBudgetIDfromUser(budgetDTO.getName(),true);
             budgetService.removeID(id);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body("{\"message\": \"Presupuesto eliminado correctamente\"}");
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editBudget(@RequestBody BudgetDTO budgetDTO) {
        System.out.println(budgetDTO);
        try{
            String idBudget = userService.returnBudgetIDfromUser(budgetDTO.getName(),false);
            budgetService.updateBudget(idBudget, budgetDTO.getBudget());

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}
