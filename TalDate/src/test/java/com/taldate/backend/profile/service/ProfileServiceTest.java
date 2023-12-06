package com.taldate.backend.profile.service;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProfileService profileService;

    private Profile profile1, profile2, profile3;
    private ProfileDTO profileDTO1, profileDTO2, profileDTO3;


    @BeforeEach
    void setUp() {
        profile1 = new Profile();
        profile1.setId(1);
        profile1.setName("John Doe");

        profile2 = new Profile();
        profile2.setId(2);
        profile2.setName("Jane Smith");

        profile3 = new Profile();
        profile3.setId(3);
        profile3.setName("Alex");

        profileDTO1 = ProfileDTO.builder()
                .name(profile1.getName())
                .age(profile1.getAge())
                .genderPreferenceMale(profile1.isGenderPreferenceMale())
                .bio(profile1.getBio())
                .picture(profile1.getPicture())
                .profileActive(profile1.isProfileActive())
                .genderMale(profile1.isGenderMale())
                .build();

        profileDTO2 = ProfileDTO.builder()
                .name(profile2.getName())
                .age(profile2.getAge())
                .genderPreferenceMale(profile2.isGenderPreferenceMale())
                .bio(profile2.getBio())
                .picture(profile2.getPicture())
                .profileActive(profile2.isProfileActive())
                .genderMale(profile2.isGenderMale())
                .build();

        profileDTO3 = ProfileDTO.builder()
                .name(profile3.getName())
                .age(profile3.getAge())
                .genderPreferenceMale(profile3.isGenderPreferenceMale())
                .bio(profile3.getBio())
                .picture(profile3.getPicture())
                .profileActive(profile3.isProfileActive())
                .genderMale(profile3.isGenderMale())
                .build();

    }

    @Test
    void getAllProfiles() {
        List<Profile> profiles = List.of(profile1, profile2);
        when(profileRepository.findAll()).thenReturn(profiles);
        when(userMapper.profileToProfileDTO(profile1)).thenReturn(profileDTO1);
        when(userMapper.profileToProfileDTO(profile2)).thenReturn(profileDTO2);

        List<ProfileDTO> result = profileService.getAllProfiles();

        assertEquals(2, result.size());
    }

    @Test
    void getProfileById() {
        Integer profileId = profile1.getId();
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile1));
        when(userMapper.profileToProfileDTO(profile1)).thenReturn(profileDTO1);

        ProfileDTO result = profileService.getProfileById(profileId);

        assertNotNull(result, "Returned ProfileDTO should not be null");
        assertEquals(profileDTO1, result, "The returned ProfileDTO does not match the expected value");
        verify(profileRepository).findById(profileId);
        verify(userMapper).profileToProfileDTO(profile1);
    }

    @Test
    void updateProfile() {
        Integer profileId = profile1.getId();
        ProfileDTO updatedProfileDTO = ProfileDTO.builder()
                .name("Updated Name")
                .age(30)
                .genderPreferenceMale(true)
                .bio("Updated Bio")
                .picture("Updated Picture URL")
                .profileActive(true)
                .genderMale(profile1.isGenderMale())
                .build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile1));

        profileService.updateProfile(profileId, updatedProfileDTO);

        verify(profileRepository).findById(profileId);
        verify(profileRepository).save(profile1);
    }

    @Test
    void getRandomProfile() {
        Integer userId = profile1.getId();
        List<Profile> activeProfiles = List.of(profile1, profile2, profile3);

        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile1));
        when(profileRepository.findByProfileActiveTrue()).thenReturn(activeProfiles);

        lenient().when(userMapper.profileToProfileDTO(profile2)).thenReturn(profileDTO2);
        lenient().when(userMapper.profileToProfileDTO(profile3)).thenReturn(profileDTO3);

        ProfileDTO result = profileService.getRandomProfile(userId);

        assertNotNull(result);
        verify(profileRepository).findById(userId);
        verify(profileRepository).findByProfileActiveTrue();
    }
}