package com.taldate.backend.user.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND_MESSAGE));
        return userMapper.userToUserDTO(user);
    }

    public UserDTO updatePassword(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND_MESSAGE));
        user.setPasswordHash(userDTO.passwordHash());
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public UserDTO updateEmail(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND_MESSAGE));
        user.setEmail(userDTO.email());
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public UserDTO updateName(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND_MESSAGE));
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public void deleteUserByID(Integer id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> userRepository.deleteById(id),
                () -> {
                    throw new ApplicationException(USER_NOT_FOUND_MESSAGE);
                }
        );
        userRepository.deleteById(id);
    }
}
