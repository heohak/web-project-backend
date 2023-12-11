package com.taldate.backend.profile.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.match.entity.Match;
import com.taldate.backend.match.repository.MatchRepository;
import com.taldate.backend.picture.Picture;
import com.taldate.backend.picture.PictureRepository;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.dto.UpdateBioDTO;
import com.taldate.backend.profile.dto.UpdateGenderPreferenceDTO;
import com.taldate.backend.profile.dto.UpdateProfilePictureDTO;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private MatchRepository matchRepository;

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

        profileDTO = ProfileDTO.builder().name("John Doe").age(33).genderPreferenceMale(true).bio("Sample Bio").picture("encoded_picture_data").profileActive(true).genderMale(true).build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(1);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void get_current_profile_dto_success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        when(profileMapper.profileToProfileDTO(currentProfile)).thenReturn(profileDTO);

        ProfileDTO result = profileService.getCurrentProfileDTO();

        assertEquals(profileDTO, result);
    }

    @Test
    void get_current_profile_dto_profile_not_found() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> profileService.getCurrentProfileDTO());
    }

    @Test
    void update_gender_preference_success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        UpdateGenderPreferenceDTO dto = new UpdateGenderPreferenceDTO(false);

        profileService.updateGenderPreference(dto);

        assertFalse(currentProfile.isGenderPreferenceMale());
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void update_bio_success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));
        UpdateBioDTO dto = new UpdateBioDTO("New Bio");

        profileService.updateBio(dto);

        assertEquals("New Bio", currentProfile.getBio());
        verify(profileRepository).save(currentProfile);
    }

    @Test
    void update_profile_picture_success() {
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
    void get_random_profile_no_matching_profiles() {
        currentProfile.setGenderPreferenceMale(true);
        when(profileRepository.findById(1)).thenReturn(Optional.of(currentProfile));

        List<Profile> activeProfiles = new ArrayList<>();
        Profile matchedProfile1 = new Profile();
        matchedProfile1.setId(2);
        matchedProfile1.setName("Jane Doe");
        matchedProfile1.setGenderMale(false);
        activeProfiles.add(matchedProfile1);

        Match positiveMatch = new Match();
        positiveMatch.setProfile1(currentProfile);
        positiveMatch.setProfile2(matchedProfile1);
        positiveMatch.setMatchedByBoth(true);


        when(profileRepository.findByProfileActiveTrue()).thenReturn(activeProfiles);

        when(matchRepository.findAllPositiveMatches(1)).thenReturn(List.of(positiveMatch));

        assertThrows(ApplicationException.class, () -> profileService.getRandomProfile());
    }


    @Test
    void get_random_profile_no_active_profiles() {
        lenient().when(profileRepository.findByProfileActiveTrue()).thenReturn(List.of());

        assertThrows(ApplicationException.class, () -> profileService.getRandomProfile());
    }

    @Test
    void calculate_age_success() {
        LocalDate thirtyYearsAgo = LocalDate.now().minusYears(30);
        Date birthDate = Date.valueOf(thirtyYearsAgo);

        int age = profileService.calculateAge(birthDate);

        assertEquals(30, age);
    }

    @Test
    void combine_full_name_success() {
        String firstName = "John";
        String lastName = "Doe";

        String fullName = profileService.combineFullName(firstName, lastName);

        assertEquals("John Doe", fullName);
    }

}
