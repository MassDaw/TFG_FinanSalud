package com.proyecto.tfg_finansalud.controladores;

import com.proyecto.tfg_finansalud.DTO.budget.BudgetDTO;
import com.proyecto.tfg_finansalud.DTO.budget.BudgetMapper;
import com.proyecto.tfg_finansalud.controller.RestBudgetController;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.services.BudgetService;
import com.proyecto.tfg_finansalud.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestBudgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BudgetMapper budgetMapper;

    @MockitoBean
    private BudgetService budgetService;

    @WithMockUser("pao")
    @Test
    void testGetAllBudgets_returnsOk() throws Exception {
        // Simulamos los datos
        Budget budget = Budget.builder().build();
        BudgetDTO dto = new BudgetDTO(); // agrega datos si quieres

        when(userService.getBudget()).thenReturn(List.of(budget));
        when(budgetMapper.toDTOList(List.of(budget))).thenReturn(List.of(dto));

        mockMvc.perform(get("/budget/getAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser("pao")
    @Test
    void testNewBudget_returnsCreated() throws Exception {
        BudgetDTO dto = new BudgetDTO();
        dto.setName("entretenimiento");

        Budget budget = Budget.builder().build();
        budget.setName("entretenimiento");

        when(budgetMapper.toEntity(dto)).thenReturn(budget);

        mockMvc.perform(post("/budget/new")
                        .contentType("application/json")
                        .content("""
                {
                    "name": "entretenimiento"
                }
            """))
                .andExpect(status().isCreated());
    }

    @WithMockUser("pao")
    @Test
    void testDeleteBudget_returnsOk() throws Exception {
        BudgetDTO dto = new BudgetDTO();
        dto.setName("entretenimiento");

        when(userService.returnBudgetIDfromUser("entretenimiento", true)).thenReturn("123");

        mockMvc.perform(delete("/budget/delete")
                        .contentType("application/json")
                        .content("""
                {
                    "name": "entretenimiento"
                }
            """))
                .andExpect(status().isOk());
    }

    @WithMockUser("pao")
    @Test
    void testEditBudget_returnsOk() throws Exception {
        BudgetDTO dto = new BudgetDTO();
        dto.setName("entretenimiento");
        dto.setBudget(500.0);

        when(userService.returnBudgetIDfromUser("entretenimiento", false)).thenReturn("123");

        mockMvc.perform(put("/budget/edit")
                        .contentType("application/json")
                        .content("""
                {
                    "name": "entretenimiento",
                    "budget": 500.0
                }
            """))
                .andExpect(status().isOk());
    }






}
