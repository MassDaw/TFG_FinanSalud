package com.proyecto.tfg_finansalud.services;

import com.proyecto.tfg_finansalud.entities.Budget;
import com.proyecto.tfg_finansalud.entities.Item;
import com.proyecto.tfg_finansalud.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ItemService{
    final ItemRepository itemRepository;

    public Item save(Item item){
        return itemRepository.save(item);
    }


}
