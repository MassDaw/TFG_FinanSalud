package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.User;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User Save(User user) {
        return userRepository.save(user);
    }
}
