package com.taldate.backend.match.service;

import com.taldate.backend.match.dto.MatchDTO;
import com.taldate.backend.match.entity.Match;
import com.taldate.backend.match.repository.MatchRepository;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.mapper.ProfileMapper;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private MatchService matchService;


    @Test
    void match_Successful() {
        Profile currentProfile = new Profile();
        currentProfile.setId(1);
        Profile otherProfile = new Profile();
        otherProfile.setId(2);

        when(profileService.getCurrentProfile()).thenReturn(currentProfile);
        when(profileRepository.findById(2)).thenReturn(Optional.of(otherProfile));
        when(matchRepository.findPotentialMatch(1, 2)).thenReturn(Optional.empty());

        MatchDTO dto = new MatchDTO(2);
        matchService.match(dto);

        verify(matchRepository).save(any(Match.class));
    }

    @Test
    void getAllMatches_Successful() {
        Profile currentProfile = new Profile();
        currentProfile.setId(1);
        Profile otherProfile = new Profile();
        otherProfile.setId(2);
        Match match = new Match();
        match.setProfile1(currentProfile);
        match.setProfile2(otherProfile);

        when(profileService.getCurrentProfile()).thenReturn(currentProfile);
        when(matchRepository.findAllPositiveMatches(1)).thenReturn(List.of(match));
        when(profileRepository.findById(2)).thenReturn(Optional.of(otherProfile));

        ProfileDTO mockProfileDTO = new ProfileDTO("John Doe", 30, true, "Bio", "pictureUrl", true, true);
        when(profileMapper.profileToProfileDTO(otherProfile)).thenReturn(mockProfileDTO);

        List<ProfileDTO> matches = matchService.getAllMatches();

        assertFalse(matches.isEmpty());
        verify(profileMapper).profileToProfileDTO(otherProfile);
    }

    @Test
    void match_WithNonExistentUser() {
        Profile currentProfile = new Profile();
        currentProfile.setId(1);
        when(profileService.getCurrentProfile()).thenReturn(currentProfile);

        when(profileRepository.findById(2)).thenReturn(Optional.empty());

        MatchDTO dto = new MatchDTO(2);
        matchService.match(dto);

        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void getAllMatches_NonExistentProfile() {
        int currentProfileId = 1;
        int otherProfileId = 2;

        Profile currentProfile = new Profile();
        currentProfile.setId(currentProfileId);

        Profile otherProfile = new Profile();
        otherProfile.setId(otherProfileId);

        Match match = new Match();
        match.setProfile1(currentProfile);
        match.setProfile2(otherProfile);

        when(profileService.getCurrentProfile()).thenReturn(currentProfile);
        when(matchRepository.findAllPositiveMatches(currentProfileId)).thenReturn(List.of(match));
        when(profileRepository.findById(otherProfileId)).thenReturn(Optional.empty()); // Simulating missing profile

        List<ProfileDTO> matches = matchService.getAllMatches();

        assertTrue(matches.isEmpty());
        verify(profileRepository).findById(otherProfileId);
    }




}
