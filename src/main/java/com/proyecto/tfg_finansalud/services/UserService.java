package com.proyecto.tfg_finansalud.services;

import com.mongodb.client.result.UpdateResult;
import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Income;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Usuario user) throws Exception {
        Optional<Usuario> usuario = userRepository.findByUsername(user.getUsername());
        if (usuario.isPresent()) {
            throw new Exception("Usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //Obtiene todos los presupuestos del usuario del MES EN CURSO
    public List<Budget> getBudget() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream()
                .filter(a->a.getYearMonth().equals(YearMonth.now().atDay(1))).collect(Collectors.toList());
    }
    //Obtiene todos los presupuestos del usuario del MES EN CURSO Y solo retorna los id
    public List<String> getBudgetID() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream()
                .filter(a->a.getYearMonth().equals(YearMonth.now().atDay(1))).map(Budget::getId).collect(Collectors.toList());
    }
    public List<String> getIncomeID() {
        return userRepository.findByUsername(getAuthenticatedUsername())
                .map(user -> Optional.ofNullable(user.getIncome())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(income -> Optional.ofNullable(income.getDate())
                                .map(date -> date.equals(YearMonth.now().atDay(1)))
                                .orElse(false))
                        .map(Income::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<String> getAllBudgetID() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream().map(Budget::getId).collect(Collectors.toList());
    }

    //ASIGNA UN NUEVO BUDGET al usuario
    public void userNewBudget(Budget budget) throws Exception {
        String username = getAuthenticatedUsername();
        Query query = new Query(Criteria.where("username").is(username));

        Update update = new Update().push("budgets", budget);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Usuario.class);

        if (result.getModifiedCount() == 0) {
            throw new Exception("Usuario no encontrado o no modificado");
        }
    }
    //BASADO EN EL NOMBRE DEL BUDGET, devuelve el ID del budget que sea del mes actual
    //Recibe un boolean que opcionalmente puede borrar el budget o no
    public String returnBudgetIDfromUser(String budgetName, boolean remove) throws Exception {
        try {
            Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));
            if (user.isPresent()) {
                //el budget borrado debe coincidir con el NOMBRE Y FECHA ACTUAL PARA QUE NO SE ELIMINE REGISTROS ANTERIORES

                Optional<Budget> budget = user.get().getBudgets().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(budgetName) && a.getYearMonth().equals(YearMonth.now().atDay(1)))
                        .findFirst();
                if (budget.isEmpty()) {
                    return "nope";
                }
                if (remove)user.get().getBudgets().remove(budget.get());
                userRepository.save(user.get());
                return budget.get().getId();
            }
        }catch (Exception e){
            throw new Exception("Usuario no encontrado");
        }
        throw new Exception("Usuario no encontrado");
    }



    //obtener nombre de usuario autenticado
    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // Si el principal es solo un string
        }
    }
    public Income getIncomeByBudgetName(String budgetName) throws Exception {
        Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));
        if (user.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        System.out.println(user.get().getIncome());
        List<String> incomeIds = Optional.ofNullable(user.get().getIncome())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)  // Filtra elementos nulos
                .map(Income::getId)
                .collect(Collectors.toList());



        LocalDate now = LocalDate.now();
        Query query = new Query();
        query.addCriteria(
                Criteria.where("id").in(incomeIds)
                        .and("date").gte(now.withDayOfMonth(1)) // desde el primer día del mes
                        .lte(now.withDayOfMonth(now.lengthOfMonth()))
                        .and("name").is(budgetName)// hasta el último día del mes
        );

        return mongoTemplate.findOne(query, Income.class);
    }

    public Double getIncome (){
        Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));

        List<Income> income = user.get().getIncome().stream().filter(a -> a.getDate().getMonth().equals(LocalDate.now().getMonth())).toList();



        return income.stream().mapToDouble(Income::getIncomes).sum();
    }

    public void userNewIncome(Income income) throws Exception {
        String username = getAuthenticatedUsername();
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().push("income", income);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Usuario.class);

        if (result.getModifiedCount() == 0) {
            throw new Exception("Usuario no encontrado o no modificado");
        }
    }
}