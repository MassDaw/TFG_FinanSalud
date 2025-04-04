package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void save(Usuario user) throws Exception {
        Optional<Usuario> usuario = userRepository.findByUsername(user.getUsername());
        if (usuario.isPresent()) {
            throw new Exception("Usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<Budget> getBudget() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets();
    }


    public void userNewBudget(Budget budget) throws Exception {

        Optional<Usuario> user = userRepository.findByUsername(getAuthenticatedUsername());
        if (user.isPresent()) {
            user.get().getBudgets().add(budget);
            userRepository.save(user.get());
        }
        else {
            throw new Exception("Usuario no encontrado");
        }
    }

    public String returnBudgetIDfromUser(String budgetName, boolean remove) throws Exception {
        try {
            Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));
            if (user.isPresent()) {
                //el budget borrado debe coincidir con el NOMBRE Y FECHA ACTUAL PARA QUE NO SE ELIMINE REGISTROS ANTERIORES
                Optional<Budget> budget = user.get().getBudgets().stream()
                        .filter(a -> a.getName().equals(budgetName) && a.getYearMonth().equals(YearMonth.now().atDay(1)))
                        .findFirst();

                if (remove)user.get().getBudgets().remove(budget.get());
                userRepository.save(user.get());
                return budget.get().getId();
            }
        }catch (Exception e){
            throw new Exception("Usuario no encontrado");
        }
        throw new Exception("Budget no encontrado");
    }



    //obtener nombre de usuario autenticado
    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // Si el principal es solo un string (caso raro)
        }
    }

}
