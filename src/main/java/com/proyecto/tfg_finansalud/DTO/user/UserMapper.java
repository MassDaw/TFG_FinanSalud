package com.proyecto.tfg_finansalud.DTO.user;

import com.proyecto.tfg_finansalud.entities.User;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> userListToUserDTOList(List<User> users);
    List<User> userDTOListToUserList(List<UserDTO> userDTOs);
}
