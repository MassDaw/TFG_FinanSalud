package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.DTO.Item.ItemDTO;
import com.proyecto.tfg_finansalud.DTO.Item.ItemMapper;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.ItemService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ItemMapper itemMapper;
    private final ItemService itemService;

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
    @PostMapping("/newItem/{budget}")
    public ResponseEntity<?> createItem(@RequestBody ItemDTO itemDTO, @PathVariable String budget) {
        try{
            Item newItem = itemMapper.tOEntity(itemDTO);
            String budgetID = userService.returnBudgetIDfromUser(budget, false);
            if (budgetID.equals("nope")) {
                budgetService.save(Budget.builder().name(budget).budget(newItem.getItemPrice()).color("red").build());
                budgetID = userService.returnBudgetIDfromUser(budget, false);
            }
            System.out.println(budgetID);
            Budget budget1 = budgetService.getBudget(budgetID);
            if (budget1 == null) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            itemService.save(newItem);
            budget1.getItems().add(newItem);
            budgetService.save(budget1);

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
