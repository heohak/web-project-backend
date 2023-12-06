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

import java.util.List;
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
        // Validations
        // ...

        updateGenderPreference(id, profileDTO);
        updateBio(id, profileDTO);
        updatePicture(id, profileDTO);
        setProfileActive(id);
    }

    private void updateGenderPreference(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating gender preference for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setGenderPreferenceMale(profileDTO.genderPreferenceMale());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    private void updateBio(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating bio for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setBio(profileDTO.bio());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    private void updatePicture(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating picture for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setPicture(profileDTO.picture());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    public void setProfileActive(Integer id) {
        log.debug("Setting profile active for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setProfileActive(true);
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    public ProfileDTO getRandomProfile() {
        List<Profile> activeProfiles = profileRepository.findByProfileActiveTrue();
        if (activeProfiles.isEmpty()) {
            throw new ApplicationException("No active profiles available.");
        }
        Profile randomProfile = activeProfiles.get(random.nextInt(activeProfiles.size()));
        return userMapper.profileToProfileDTO(randomProfile);
    }
}
