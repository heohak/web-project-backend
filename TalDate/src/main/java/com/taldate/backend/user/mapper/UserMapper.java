package com.taldate.backend.user.mapper;

import com.taldate.backend.auth.dto.RegisterDTO;
import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "id", ignore = true)
    User registerDTOtoUser(RegisterDTO registerDTO);
}
