package com.proyecto.tfg_finansalud.repositories;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String>{
}
