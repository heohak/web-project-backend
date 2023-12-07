package com.taldate.backend.auth.service;


import com.taldate.backend.auth.dto.LoginDTO;
import com.taldate.backend.auth.dto.LoginResponseDTO;
import com.taldate.backend.auth.dto.RegisterDTO;
import com.taldate.backend.auth.jwt.JwtUtils;
import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.profile.service.ProfileService;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Spy
    private UserMapper userMapper;

    private RegisterDTO registerDto;
    private LoginDTO loginDto;
    private String testToken = "testToken";


    @BeforeEach
    void setUp() {
        registerDto = new RegisterDTO(
                "John", "Doe", "john@example.com", "password123",
                Date.valueOf("1990-01-01"), true);

        loginDto = new LoginDTO("john@example.com", "password123");


    }

    @Test
    void register() {
        User user = new User();
        Profile profile = new Profile();

        when(userRepository.findByEmail(registerDto.email().toLowerCase())).thenReturn(Optional.empty());
        when(userMapper.registerDTOtoUser(registerDto)).thenReturn(user);
        when(profileService.getFullName(registerDto.firstName(), registerDto.lastName())).thenReturn("John Doe");
        when(profileService.getAge(registerDto.dateOfBirth())).thenReturn(30);

        assertDoesNotThrow(() -> authService.register(registerDto));
        verify(userRepository).save(any(User.class));
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void login() {
        User user = new User();
        user.setId(1);
        user.setPasswordHash(passwordEncoder.encode("password123"));

        when(userRepository.findByEmail(loginDto.email().toLowerCase())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.password(), user.getPasswordHash())).thenReturn(true);
        when(jwtUtils.generateToken(user.getId())).thenReturn(testToken);

        LoginResponseDTO response = authService.login(loginDto);

        assertNotNull(response);
        assertEquals(testToken, response.token());
        verify(jwtUtils).generateToken(user.getId());
    }

    @Test
    void loginFailed() {
        User user = new User();
        user.setId(1);
        user.setPasswordHash(passwordEncoder.encode("correctPassword"));

        when(userRepository.findByEmail(loginDto.email().toLowerCase())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.password(), user.getPasswordHash())).thenReturn(false);

        ApplicationException thrown = assertThrows(ApplicationException.class, () -> authService.login(loginDto));
        assertEquals("Wrong username or password.", thrown.getMessage());
    }
}