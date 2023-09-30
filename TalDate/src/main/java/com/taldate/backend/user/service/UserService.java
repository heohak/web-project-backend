package com.taldate.backend.user.service;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO updatePassword(Integer id, UserDTO userDTO) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            user.setPassword_hash(userDTO.password_hash());
            userRepository.save(user);
            return userDTO;
    }

    public UserDTO updateEmail(Integer id, UserDTO userDTO) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            user.setEmail(userDTO.email());
            userRepository.save(user);
            return userDTO;
    }

    public UserDTO updateName(Integer id, UserDTO userDTO) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            user.setFirstName(userDTO.firstName());
            user.setLastName(userDTO.lastName());
            userRepository.save(user);
            return userDTO;
    }

    public void deleteUserByID(Integer id) {
        userRepository.deleteById(id);
    }
}
