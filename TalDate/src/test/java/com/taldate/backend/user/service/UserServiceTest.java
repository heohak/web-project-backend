package com.taldate.backend.user.service;

import com.taldate.backend.profile.service.ProfileService;
import com.taldate.backend.user.dto.*;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileService profileService;
    @InjectMocks
    private UserService userService;

    private User currentUser;
    private Profile currentProfile;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setFirstName("John");
        currentUser.setLastName("Doe");
        currentUser.setEmail("john.doe@example.com");
        currentUser.setPasswordHash("hashedPassword");
        currentUser.setDateOfBirth(Date.valueOf("1990-01-01"));
        currentUser.setGenderMale(true);

        currentProfile = new Profile();
        currentUser.setProfile(currentProfile);

        userDTO = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword")
                .dateOfBirth(Date.valueOf("1990-01-01"))
                .genderMale(true)
                .build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(1);
    }

    @Test
    void getAllUsers() {

        List<User> users = Arrays.asList(currentUser);
        List<UserDTO> userDTOs = Arrays.asList(userDTO);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserDTO(currentUser)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDTOs, result);

        }

    @Test
    void getUsers_WithoutSearch() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
        Page<User> page = new PageImpl<>(Arrays.asList(currentUser));

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        Page<UserDTO> result = userService.getUsers(0, 10, "firstName", "asc", "");

        assertEquals(1, result.getContent().size());
        assertEquals(userDTO, result.getContent().get(0));
        verify(userRepository).findAll(pageable);
    }



    @Test
    void updateEmail_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        UpdateEmailDTO dto = new UpdateEmailDTO("new.email@example.com");
        userService.updateEmail(dto);

        assertEquals("new.email@example.com", currentUser.getEmail());
        verify(userRepository).save(currentUser);
    }

    @Test
    void updatePassword_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        UpdatePasswordDTO dto = new UpdatePasswordDTO("newPassword");
        userService.updatePassword(dto);

        assertEquals("encodedNewPassword", currentUser.getPasswordHash());
        verify(userRepository).save(currentUser);
    }




    @Test
    void updateFirstName_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        UpdateFirstNameDTO dto = new UpdateFirstNameDTO("NewFirstName");
        userService.updateFirstName(dto);

        assertEquals("NewFirstName", currentUser.getFirstName());
        verify(userRepository).save(currentUser);
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void updateLastName_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        UpdateLastNameDTO dto = new UpdateLastNameDTO("NewLastName");
        userService.updateLastName(dto);

        assertEquals("NewLastName", currentUser.getLastName());
        verify(userRepository).save(currentUser);
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void updateDateOfBirth_Success() {
        Date newDateOfBirth = Date.valueOf("2000-01-01");
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        UpdateDateOfBirthDTO dto = new UpdateDateOfBirthDTO(newDateOfBirth);
        userService.updateDateOfBirth(dto);

        assertEquals(newDateOfBirth, currentUser.getDateOfBirth());
        verify(userRepository).save(currentUser);
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void updateGender_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        UpdateGenderDTO dto = new UpdateGenderDTO(true);
        userService.updateGender(dto);

        assertTrue(currentUser.isGenderMale());
        verify(userRepository).save(currentUser);
    }
}
