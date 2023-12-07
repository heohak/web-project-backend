package com.taldate.backend.user.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.user.dto.*;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        int id = (int) (context.getAuthentication().getPrincipal());
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found while in service layer");
                    return new ApplicationException("User not found");
                });
    }

    @Transactional
    public void updateEmail(UpdateEmailDTO dto) {
        User user = getCurrentUser();
        log.info("Updating email for user with ID: {}", user.getId());
        user.setEmail(dto.newEmail());
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO dto) {
        User user = getCurrentUser();
        log.info("Updating password for user with ID: {}", user.getId());
        user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser() {
        User user = getCurrentUser();
        log.info("Deleting user with ID: {}", user.getId());
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void updateFirstName(UpdateFirstNameDTO dto) {
        User user = getCurrentUser();
        log.info("Updating first name for user with ID: {}", user.getId());
        user.setFirstName(dto.newFirstName());
        userRepository.save(user);
    }

    @Transactional
    public void updateLastName(UpdateLastNameDTO dto) {
        User user = getCurrentUser();
        log.info("Updating last name for user with ID: {}", user.getId());
        user.setLastName(dto.newLastName());
        userRepository.save(user);
    }

    @Transactional
    public void updateDateOfBirth(UpdateDateOfBirthDTO dto) {
        User user = getCurrentUser();
        log.info("Updating date of birth for user with ID: {}", user.getId());
        user.setDateOfBirth(dto.newDateOfBirth());
        userRepository.save(user);
    }

    @Transactional
    public void updateGender(UpdateGenderDTO dto) {
        User user = getCurrentUser();
        log.info("Updating gender for user with ID: {}", user.getId());
        user.setGenderMale(dto.genderMale());
        userRepository.save(user);
    }
}
