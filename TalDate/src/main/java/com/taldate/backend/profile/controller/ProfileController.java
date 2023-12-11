package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.*;
import com.taldate.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileDTO getCurrentProfile() {
        return profileService.getCurrentProfileDTO();
    }

    @PutMapping("/genderPreference")
    public void updateGenderPreference(@RequestBody UpdateGenderPreferenceDTO dto) {
        profileService.updateGenderPreference(dto);
    }

    @PutMapping("/bio")
    public void updateBio(@RequestBody UpdateBioDTO dto) {
        profileService.updateBio(dto);
    }

    @PutMapping("/profilePicture")
    public void updateProfilePicture(@RequestBody UpdateProfilePictureDTO dto) {
        profileService.updateProfilePicture(dto);
    }

    @GetMapping("/random")
    public ProfileSwipeResponseDTO getRandomProfile() {
        return profileService.getRandomProfile();
    }
}
