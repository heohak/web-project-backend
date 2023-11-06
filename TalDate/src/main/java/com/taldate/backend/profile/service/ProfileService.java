package com.taldate.backend.profile.service;

import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String PROFILE_NOT_FOUND_MESSAGE = "Profile not found.";
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;

    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setGenderPreference(profileDTO.genderPreference());
        profile.setBio(profileDTO.bio());
        profile.setPicture(profileDTO.picture());

        Profile savedProfile = profileRepository.save(profile);
        return userMapper.profileToProfileDTO(savedProfile);
    }

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .map(userMapper::profileToProfileDTO)
                .toList();
    }

    public ProfileDTO getProfileById(Integer id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROFILE_NOT_FOUND_MESSAGE));
        return userMapper.profileToProfileDTO(profile);
    }

    public void deleteProfileById(Integer id) {
        profileRepository.findById(id)
                .ifPresentOrElse(
                        profile -> profileRepository.deleteById(id),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PROFILE_NOT_FOUND_MESSAGE);
                        }
                );
    }

    public ProfileDTO updateGenderPreference(Integer id, ProfileDTO profileDTO) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROFILE_NOT_FOUND_MESSAGE));
        profile.setGenderPreference(profileDTO.genderPreference());
        profileRepository.save(profile);
        return userMapper.profileToProfileDTO(profile);
    }

    public ProfileDTO updateBio(Integer id, ProfileDTO profileDTO) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROFILE_NOT_FOUND_MESSAGE));
        profile.setBio(profileDTO.bio());
        profileRepository.save(profile);
        return userMapper.profileToProfileDTO(profile);
    }

    public ProfileDTO updatePicture(Integer id, ProfileDTO profileDTO) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROFILE_NOT_FOUND_MESSAGE));
        profile.setPicture(profileDTO.picture());
        profileRepository.save(profile);
        return userMapper.profileToProfileDTO(profile);
    }

}
