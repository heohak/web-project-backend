package com.taldate.backend.user.mapper;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOtoUser(UserDTO userDTO);
}
