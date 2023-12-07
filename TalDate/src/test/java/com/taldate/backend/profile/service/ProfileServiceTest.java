package com.taldate.backend.profile.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Random random;
    @InjectMocks
    private ProfileService profileService;

    private Profile currentProfile;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        currentProfile = new Profile();
        currentProfile.setId(1);
        currentProfile.setName("John Doe");
        currentProfile.setAge(33);
        currentProfile.setGenderPreferenceMale(true);
        currentProfile.setBio("Sample Bio");
        currentProfile.setPicture("sample_picture_url.jpg");
        currentProfile.setProfileActive(true);
        currentProfile.setGenderMale(true);

        // Initialize the profileDTO object with test data
        profileDTO = ProfileDTO.builder()
                .name("John Doe")
                .age(33)
                .genderPreferenceMale(true)
                .bio("Sample Bio")
                .picture("sample_picture_url.jpg")
                .profileActive(true)
                .genderMale(true)
                .build();

        // Mock SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
       lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(1); // Assuming current user has ID 1
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void getCurrentProfileDTO_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        when(userMapper.profileToProfileDTO(currentProfile)).thenReturn(profileDTO);

        ProfileDTO result = profileService.getCurrentProfileDTO();

        assertEquals(profileDTO, result);
    }

    @Test
    void getCurrentProfileDTO_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> profileService.getCurrentProfileDTO());
    }

    @Test
    void updateProfile_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));

        profileService.updateProfile(profileDTO);

        verify(profileRepository).save(currentProfile);
        // Additional assertions to verify if profile fields are correctly updated
    }

    @Test
    void getRandomProfile_Success() {
        Profile anotherProfile = new Profile();
        anotherProfile.setId(2); // Ensure this ID is different from the currentProfile ID
        anotherProfile.setGenderMale(currentProfile.isGenderPreferenceMale()); // Same gender preference as currentProfile
        anotherProfile.setProfileActive(true);

        List<Profile> activeProfiles = Arrays.asList(currentProfile, anotherProfile);

        when(profileRepository.findByProfileActiveTrue()).thenReturn(activeProfiles);
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        lenient().when(random.nextInt(activeProfiles.size())).thenReturn(1); // Return index of anotherProfile in the list

        when(userMapper.profileToProfileDTO(anotherProfile)).thenReturn(profileDTO); // Ensure this mapping is necessary

        ProfileDTO result = profileService.getRandomProfile();

        assertEquals(profileDTO, result);
    }

    @Test
    void getRandomProfile_NoActiveProfiles() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        when(profileRepository.findByProfileActiveTrue()).thenReturn(Arrays.asList());

        assertThrows(ApplicationException.class, () -> profileService.getRandomProfile());
    }

    @Test
    void getAge_Success() {
        Date birthDate = Date.valueOf(LocalDate.of(1990, 1, 1));
        int age = profileService.getAge(birthDate);

        assertEquals(33, age);
    }

    @Test
    void getFullName_Success() {
        String firstName = "John";
        String lastName = "Doe";
        String fullName = profileService.getFullName(firstName, lastName);

        assertEquals("John Doe", fullName);
    }
}
