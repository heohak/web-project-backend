package com.taldate.backend.profile.service;

import com.taldate.backend.auth.exception.ProfileNotFoundException;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String PROFILE_NOT_FOUND_MESSAGE = "Profile not found.";
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;

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
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_MESSAGE));
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
    }

    private void updateGenderPreference(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating gender preference for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setGenderPreferenceMale(profileDTO.genderPreferenceMale());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    private void updateBio(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating bio for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setBio(profileDTO.bio());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }

    private void updatePicture(Integer id, ProfileDTO profileDTO) {
        log.debug("Updating picture for profile ID: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_MESSAGE));
        profile.setPicture(profileDTO.picture());
        profileRepository.save(profile);
        userMapper.profileToProfileDTO(profile);
    }
}
