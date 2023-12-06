package com.taldate.backend.profile.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String PROFILE_NOT_FOUND_MESSAGE = "Profile not found.";
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;
    private final Random random = new Random();

    public List<ProfileDTO> getAllProfiles() {
        log.info("Retrieving all profiles");
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .map(userMapper::profileToProfileDTO)
                .toList();
    }

    public ProfileDTO getProfileById(Integer id) {
        log.info("Retrieving profile with ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));
        return userMapper.profileToProfileDTO(profile);
    }

    @Transactional
    public void updateProfile(int id, ProfileDTO profileDTO) {
        log.info("Updating profile for ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));

        profile.setGenderPreferenceMale(profileDTO.genderPreferenceMale());
        profile.setBio(profileDTO.bio());
        profile.setPicture(profileDTO.picture());
        profile.setProfileActive(true);

        profileRepository.save(profile);
    }

    public ProfileDTO getRandomProfile(Integer userId) {
        // Retrieve the current user's profile
        Profile currentUserProfile = profileRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));

        // Retrieve active profiles
        List<Profile> activeProfiles = profileRepository.findByProfileActiveTrue();
        if (activeProfiles.isEmpty()) {
            throw new ApplicationException("No active profiles available.");
        }

        // Filter profiles based on user's gender preference using a for loop
        List<Profile> matchingProfiles = new ArrayList<>();
        for (Profile profile : activeProfiles) {
            if (profile.isGenderMale() == currentUserProfile.isGenderPreferenceMale()
                    && !Objects.equals(profile.getId(), currentUserProfile.getId())) {
                matchingProfiles.add(profile);
            }
        }

        if (matchingProfiles.isEmpty()) {
            throw new ApplicationException("No matching profiles based on user's gender preference.");
        }

        // Select a random profile from the matching profiles
        Profile randomProfile = matchingProfiles.get(random.nextInt(matchingProfiles.size()));
        return userMapper.profileToProfileDTO(randomProfile);
    }
}
