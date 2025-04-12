package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findAllByIdIn(List<String> ids);
}
