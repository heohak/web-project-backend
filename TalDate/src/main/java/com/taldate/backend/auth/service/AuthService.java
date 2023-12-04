package com.taldate.backend.auth.service;

import com.taldate.backend.auth.dto.LoginDTO;
import com.taldate.backend.auth.dto.LoginResponseDTO;
import com.taldate.backend.auth.dto.RegisterDTO;
import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.auth.jwt.JwtUtils;
import com.taldate.backend.auth.validator.RegisterValidator;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public void register(RegisterDTO dto) {
        // Validate fields
        if (!RegisterValidator.isValidDto(dto)) {
            // Front end should duplicate all the checks in RegisterValidator,
            // so we shall not respond here with any specific error message
            throw new ApplicationException("Bad request");
        }

        // Avoid duplicate emails
        Optional<User> existingUser = userRepository.findByEmail(dto.email().toLowerCase());
        if (existingUser.isPresent()) {
            throw new ApplicationException("Account with this email already exists.");
        }

        // Checks passed!
        // 1. Create new default empty profile
        Profile profile = new Profile();
        profile.setGenderPreferenceMale(!dto.genderMale());
        profile.setBio("");
        profile.setPicture("");
        profileRepository.save(profile);
        // 2. Create new user account
        User user = userMapper.registerDTOtoUser(dto);
        user.setEmail(dto.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setProfile(profile);
        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginDTO dto) {
        // Validate fields to do

        Optional<User> user = userRepository.findByEmail(dto.email().toLowerCase());
        if (user.isEmpty() || !passwordEncoder.matches(dto.password(), user.get().getPasswordHash())) {
            throw new ApplicationException("Wrong username or password.");
        }

        // Generate token
        return new LoginResponseDTO(jwtUtils.generateToken(user.get().getId()));
    }
}

