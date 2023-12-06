package com.taldate.backend.user.service;

import com.taldate.backend.user.dto.UserDTO;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Spy
    private UserMapper userMapper;

    private User user1, user2;
    private UserDTO userDTO1, userDTO2;

    @BeforeEach
    void setUp() {
        Date sampleDateOfBirth = Date.valueOf("1990-01-01");
        user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPasswordHash("hashedPassword1");
        user1.setDateOfBirth(sampleDateOfBirth);

        user2 = new User();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPasswordHash("hashedPassword2");
        user2.setDateOfBirth(sampleDateOfBirth);

        userDTO1 = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword1")
                .dateOfBirth(sampleDateOfBirth)
                .genderMale(true)
                .build();
        userDTO2 = UserDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .passwordHash("hashedPassword2")
                .dateOfBirth(sampleDateOfBirth)
                .genderMale(false)
                .build();

    }

    @Test
    void getAllUsers() {

        List<User> users = Arrays.asList(user1, user2);
        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserDTO(user1)).thenReturn(userDTO1);
        when(userMapper.userToUserDTO(user2)).thenReturn(userDTO2);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDTOs, result);

    }

    @Test
    void getUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userMapper.userToUserDTO(user1)).thenReturn(userDTO1);

        UserDTO result = userService.getUserById(1);

        assertEquals(userDTO1, result);
    }

    @Test
    void updatePassword() {
        Integer userId = 1;
        String newPassword = "newHashedPassword";
        UserDTO updatedUserDTO = UserDTO.builder()
                .passwordHash(newPassword)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userMapper.userToUserDTO(user1)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updatePassword(userId, updatedUserDTO);

        assertEquals(newPassword, result.passwordHash());
        verify(userRepository).save(user1);

    }

    @Test
    void updateEmail() {
        Integer userId = 1;
        String newEmail = "new.email@example.com";
        UserDTO updatedUserDTO = UserDTO.builder()
                .email(newEmail)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userMapper.userToUserDTO(user1)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateEmail(userId, updatedUserDTO);

        assertEquals(newEmail, result.email());
        verify(userRepository).save(user1);
    }

    @Test
    void updateName() {
        Integer userId = 1;
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        UserDTO updatedUserDTO = UserDTO.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userMapper.userToUserDTO(user1)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateName(userId, updatedUserDTO);

        assertEquals(newFirstName, result.firstName());
        assertEquals(newLastName, result.lastName());
        verify(userRepository).save(user1);
    }

    @Test
    void deleteUserByID() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        userService.deleteUserByID(userId);

        verify(userRepository).deleteById(userId);
        assertDoesNotThrow(() -> userService.deleteUserByID(userId), "Method should not throw an exception");
    }
}