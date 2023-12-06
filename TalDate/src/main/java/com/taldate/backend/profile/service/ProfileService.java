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
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PROFILE_NOT_FOUND_MESSAGE));

        profile.setGenderPreferenceMale(profileDTO.genderPreferenceMale());
        profile.setBio(profileDTO.bio());
        profile.setPicture(profileDTO.picture());

        profileRepository.save(profile);
    }

    public ProfileDTO getRandomProfile() {
        List<Profile> profiles = profileRepository.findAll();
        if (profiles.isEmpty()) {
            throw new ApplicationException("No profiles available.");
        }
        Profile randomProfile = profiles.get(random.nextInt(profiles.size()));
        return userMapper.profileToProfileDTO(randomProfile);
    }
}
