package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> getAll() {
        return userRepository.findAll();
    }

    public void save(Usuario user) throws Exception {
        Optional<Usuario> usuario = userRepository.findByUsername(user.getUsername());
        if (usuario.isPresent()) {
            throw new Exception("Usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<Budget> getBudget() {

        List<Budget>  x = userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets();
        return x;
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
