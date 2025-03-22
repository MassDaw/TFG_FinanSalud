package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.User;
import com.proyecto.tfg_finansalud.entities.Usuario;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public List<Usuario> getAll() {
        return userRepository.findAll();
    }

    public Usuario Save(Usuario user) {
        return userRepository.save(user);
    }
}
