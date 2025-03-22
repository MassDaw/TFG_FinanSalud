package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Profile;
import com.proyecto.tfg_finansalud.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {

}
