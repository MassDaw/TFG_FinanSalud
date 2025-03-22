package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
