package com.proyecto.tfg_finansalud.DTO.Item;


import com.proyecto.tfg_finansalud.entities.Item;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface ItemMapper {
    Item tOEntity(ItemDTO itemDTO);
    ItemDTO toDTO(Item item);
}
