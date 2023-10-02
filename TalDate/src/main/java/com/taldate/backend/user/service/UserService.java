package com.taldate.backend.user.service;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPasswordHash(), user.getDateOfBirth()));
        }
        return users;
    }

    public UserDTO getUserById(Integer id) {
        UserDTO userDTO;
        try {
            User user = userRepository.getReferenceById(id);
            userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPasswordHash(), user.getDateOfBirth());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userDTO;
    }

    public UserDTO register(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.email());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        User user = new User();
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmail(userDTO.email());
        user.setPasswordHash(userDTO.passwordHash());
        user.setDateOfBirth(userDTO.dateOfBirth());

        userRepository.save(user);

        return userDTO;
    }
}
