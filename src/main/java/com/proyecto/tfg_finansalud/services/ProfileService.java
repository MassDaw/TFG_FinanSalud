package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Profile;
import com.proyecto.tfg_finansalud.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {
    final ProfileRepository repository;

    public void save(Profile profile) {repository.save(profile);}
}
