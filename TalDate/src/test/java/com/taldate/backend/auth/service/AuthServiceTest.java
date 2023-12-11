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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private RegisterDTO registerDto;
    private LoginDTO loginDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDTO(
                "John", "Doe", "john@example.com", "password123",
                java.sql.Date.valueOf("1990-01-01"), true
        );
        loginDto = new LoginDTO("john@example.com", "password123");

        User mockUser = new User();
        mockUser.setId(1);  // Set an ID for the user
        mockUser.setPasswordHash(passwordEncoder.encode(loginDto.password()));

        lenient().when(userMapper.registerDTOtoUser(any(RegisterDTO.class))).thenReturn(mockUser);
        lenient().when(userRepository.findByEmail(loginDto.email().toLowerCase())).thenReturn(Optional.of(mockUser));
        lenient().when(passwordEncoder.matches(loginDto.password(), mockUser.getPasswordHash())).thenReturn(true);
        lenient().when(jwtUtils.generateToken(mockUser.getId())).thenReturn("mockToken");
    }



    @Test
    void register_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> authService.register(registerDto));
        verify(userRepository).save(any(User.class));
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void register_InvalidData_ThrowsException() {
        RegisterDTO invalidDto = new RegisterDTO("John", "", "john@example.com", "password", java.sql.Date.valueOf("1990-01-01"), true);

        assertThrows(ApplicationException.class, () -> authService.register(invalidDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(ApplicationException.class, () -> authService.register(registerDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPasswordHash(passwordEncoder.encode(loginDto.password()));

        final String testToken = "mockToken123";

        when(userRepository.findByEmail(loginDto.email().toLowerCase())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(loginDto.password(), mockUser.getPasswordHash())).thenReturn(true);
        when(jwtUtils.generateToken(mockUser.getId())).thenReturn(testToken);

        LoginResponseDTO response = authService.login(loginDto);

        assertNotNull(response);
        assertEquals(testToken, response.token());
    }




    @Test
    void login_WrongEmail_ThrowsException() {
        when(userRepository.findByEmail(loginDto.email().toLowerCase())).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> authService.login(loginDto));
    }




}
