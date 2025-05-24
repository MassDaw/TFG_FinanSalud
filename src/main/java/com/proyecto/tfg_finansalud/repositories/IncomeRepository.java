package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Income;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncomeRepository extends MongoRepository<Income, String> {
}
