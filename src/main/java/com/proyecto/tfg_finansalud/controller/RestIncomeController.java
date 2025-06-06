package com.proyecto.tfg_finansalud.controller;


import com.proyecto.tfg_finansalud.DTO.Item.ItemDTO;
import com.proyecto.tfg_finansalud.DTO.Item.ItemMapper;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.DTO.user.UserMapper;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Income;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.IncomeService;
import com.proyecto.tfg_finansalud.services.ItemService;
import com.proyecto.tfg_finansalud.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/income")
@Slf4j
public class RestIncomeController {
    private final UserService userService;
    private final IncomeService incomeService;
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @PostMapping("/newItem/{categoria}")
    public ResponseEntity<?> newItem(@RequestBody ItemDTO itemDTO, @PathVariable String categoria) {
        Item item = itemMapper.tOEntity(itemDTO);
        item.setIncome(true);
        itemService.save(item);
        try {
            Income income = userService.getIncomeByBudgetName(categoria);
            if (income == null) {
                income = incomeService.createIncome(categoria);
                userService.userNewIncome(income);
            }
            incomeService.addItemToIncome(income.getId(), item);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Respuesta: Item creado y añadido a la categoría " + categoria);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(item, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
