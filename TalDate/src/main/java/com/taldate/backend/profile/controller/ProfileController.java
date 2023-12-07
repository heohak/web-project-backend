package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.dto.UpdateBioDTO;
import com.taldate.backend.profile.dto.UpdateGenderPreferenceDTO;
import com.taldate.backend.profile.dto.UpdateProfilePictureDTO;
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

    @PutMapping
    public void updateGenderPreference(@RequestBody UpdateGenderPreferenceDTO dto) {
        profileService.updateGenderPreference(dto);
    }

    @PutMapping
    public void updateBio(@RequestBody UpdateBioDTO dto) {
        profileService.updateBio(dto);
    }

    @PutMapping
    public void updateProfilePicture(@RequestBody UpdateProfilePictureDTO dto) {
        profileService.updateProfilePicture(dto);
    }

    @GetMapping("/random")
    public ProfileDTO getRandomProfile() {
        return profileService.getRandomProfile();
    }
}
