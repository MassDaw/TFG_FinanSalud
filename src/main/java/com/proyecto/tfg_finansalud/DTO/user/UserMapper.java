package com.proyecto.tfg_finansalud.DTO.user;

import com.proyecto.tfg_finansalud.entities.Usuario;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface UserMapper {
    UserDTO userToUserDTO(Usuario user);
    Usuario userDTOToUser(UserDTO userDTO);

    List<UserDTO> userListToUserDTOList(List<Usuario> users);
    List<Usuario> userDTOListToUserList(List<UserDTO> userDTOs);
}
