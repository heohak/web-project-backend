package com.taldate.backend.profile.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.picture.Picture;
import com.taldate.backend.picture.PictureRepository;
import com.taldate.backend.profile.dto.*;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.mapper.ProfileMapper;
import com.taldate.backend.profile.repository.ProfileRepository;
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
    private PictureRepository pictureRepository;

    @Mock
    private ProfileMapper profileMapper;

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
        currentProfile.setPicture(new Picture());
        currentProfile.setProfileActive(true);
        currentProfile.setGenderMale(true);

        profileDTO = ProfileDTO.builder()
                .name("John Doe")
                .age(33)
                .genderPreferenceMale(true)
                .bio("Sample Bio")
                .picture("encoded_picture_data")
                .profileActive(true)
                .genderMale(true)
                .build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(1);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getCurrentProfileDTO_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        when(profileMapper.profileToProfileDTO(currentProfile)).thenReturn(profileDTO);

        ProfileDTO result = profileService.getCurrentProfileDTO();

        assertEquals(profileDTO, result);
    }

    @Test
    void getCurrentProfileDTO_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> profileService.getCurrentProfileDTO());
    }

    @Test
    void updateGenderPreference_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        UpdateGenderPreferenceDTO dto = new UpdateGenderPreferenceDTO(false);

        profileService.updateGenderPreference(dto);

        assertFalse(currentProfile.isGenderPreferenceMale());
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void updateBio_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        UpdateBioDTO dto = new UpdateBioDTO("New Bio");

        profileService.updateBio(dto);

        assertEquals("New Bio", currentProfile.getBio());
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void updateProfilePicture_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        Picture newPicture = new Picture();
        newPicture.setEncodedPicture("encoded_picture_data");
        UpdateProfilePictureDTO dto = new UpdateProfilePictureDTO("encoded_picture_data");

        when(pictureRepository.save(any(Picture.class))).thenReturn(newPicture);

        profileService.updateProfilePicture(dto);

        assertNotNull(currentProfile.getPicture());
        assertEquals("encoded_picture_data", currentProfile.getPicture().getEncodedPicture());
        assertTrue(currentProfile.isProfileActive());
        verify(profileRepository).save(currentProfile);
        verify(pictureRepository).save(any(Picture.class));
    }

    @Test
    void getRandomProfile_Success() {
        Profile anotherProfile = new Profile();
        anotherProfile.setId(2);
        anotherProfile.setName("Jane Doe");
        anotherProfile.setAge(29);
        anotherProfile.setBio("Another Sample Bio");
        anotherProfile.setPicture(new Picture()); // Assuming Picture is set up correctly
        anotherProfile.setGenderMale(!currentProfile.isGenderPreferenceMale()); // Opposite gender preference
        anotherProfile.setProfileActive(true);

        List<Profile> activeProfiles = Arrays.asList(currentProfile, anotherProfile);
        when(profileRepository.findByProfileActiveTrue()).thenReturn(activeProfiles);
        when(random.nextInt(anyInt())).thenReturn(1);

        ProfileSwipeResponseDTO expectedDto = new ProfileSwipeResponseDTO(
                anotherProfile.getId(), anotherProfile.getName(),
                anotherProfile.getAge(), anotherProfile.getBio(), "picture_url");
        when(profileMapper.profileToProfileSwipeResponseDTO(anotherProfile)).thenReturn(expectedDto);

        ProfileSwipeResponseDTO result = profileService.getRandomProfile();

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }



    @Test
    void getRandomProfile_NoActiveProfiles() {
        lenient().when(profileRepository.findByProfileActiveTrue()).thenReturn(Arrays.asList());

        assertThrows(ApplicationException.class, () -> profileService.getRandomProfile());
    }
    @Test
    void calculateAge_Success() {
        LocalDate thirtyYearsAgo = LocalDate.now().minusYears(30);
        Date birthDate = Date.valueOf(thirtyYearsAgo);

        int age = profileService.calculateAge(birthDate);

        assertEquals(30, age);
    }

    @Test
    void combineFullName_Success() {
        String firstName = "John";
        String lastName = "Doe";

        String fullName = profileService.combineFullName(firstName, lastName);

        assertEquals("John Doe", fullName);
    }

}
