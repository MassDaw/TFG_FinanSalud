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
    //Muestra todos los gastos de todas las categorías
    public ResponseEntity<Map<String, List<Item>>> getItemsCurrentMonth() {
        try {
            List<String> budgetsID = userService.getBudgetID();//id de Budget del usuario en el mes actual
            return ResponseEntity.ok(
                    budgetService.getItemfromBudget(budgetsID));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/newItem/{budgetID}")
    public ResponseEntity<?> createItem(@RequestBody ItemDTO itemDTO, @PathVariable String budgetID) {
        try{
            Item newItem = itemMapper.tOEntity(itemDTO);
            Budget budget1 = budgetService.getBudget(budgetID);
            if (budget1 == null) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            itemService.save(newItem);
            System.out.println(newItem);
            budget1.getItems().add(newItem);

            budget1.setBudgetCount(budget1.getBudgetCount() + newItem.getItemPrice());
            budgetService.save(budget1);
            System.out.println(budget1);

        }catch (Exception e){
            log.error("e: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("------------------------------------------funciona");
        return ResponseEntity.ok().build();
    }
}
