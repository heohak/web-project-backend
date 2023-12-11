package com.taldate.backend.profile.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.profile.dto.*;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.mapper.ProfileMapper;
import com.taldate.backend.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final Random random = new Random();

    public Profile getCurrentProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        int id = (int) (context.getAuthentication().getPrincipal());

        return profileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Profile not found while in service layer");
                    return new ApplicationException("Profile not found");
                });
    }

    public ProfileDTO getCurrentProfileDTO() {
        return profileMapper.profileToProfileDTO(getCurrentProfile());
    }

    @Transactional
    public void updateProfile(ProfileDTO profileDTO) {
        Profile profile = getCurrentProfile();
        log.info("Updating profile for ID: {}", profile.getId());

        profile.setGenderPreferenceMale(profileDTO.genderPreferenceMale());
        profile.setBio(profileDTO.bio());
        profile.setPicture(profileDTO.picture());
        profile.setProfileActive(true);

        profileRepository.save(profile);
    }

    @Transactional
    public void updateGenderPreference(UpdateGenderPreferenceDTO dto) {
        Profile profile = getCurrentProfile();
        log.info("Updating gender preference for ID: {}", profile.getId());

        profile.setGenderPreferenceMale(dto.newGenderPreferenceMale());
        profileRepository.save(profile);
    }

    @Transactional
    public void updateBio(UpdateBioDTO dto) {
        Profile profile = getCurrentProfile();
        log.info("Updating bio for ID: {}", profile.getId());

        profile.setBio(dto.newBio());
        profileRepository.save(profile);
    }

    @Transactional
    public void updateProfilePicture(UpdateProfilePictureDTO dto) {
        Profile profile = getCurrentProfile();
        log.info("Updating profile picture for ID: {}", profile.getId());

        profile.setPicture(dto.newProfilePicture());
        profile.setProfileActive(true);
        profileRepository.save(profile);
    }

    public ProfileSwipeResponseDTO getRandomProfile() {
        Profile profile = getCurrentProfile();
        log.info("Getting random profile for ID: {}", profile.getId());

        List<Profile> activeProfiles = profileRepository.findByProfileActiveTrue();
        if (activeProfiles.isEmpty()) {
            throw new ApplicationException("No active profiles available, please try again later");
        }

        List<Profile> matchingProfiles = new ArrayList<>();
        for (Profile other : activeProfiles) {
            if (other.isGenderMale() == profile.isGenderPreferenceMale()
                    && !Objects.equals(other.getId(), profile.getId())) {
                matchingProfiles.add(other);
            }
        }
        if (matchingProfiles.isEmpty()) {
            throw new ApplicationException("No matching profiles available, please try again later");
        }

        Profile randomProfile = matchingProfiles.get(random.nextInt(matchingProfiles.size()));
        return profileMapper.profileToProfileSwipeResponseDTO(randomProfile);
    }

    /* Helpers */
    public int calculateAge(Date date) {
        LocalDate birthDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period p = Period.between(birthDate, now);
        return p.getYears();
    }

    public String combineFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}
