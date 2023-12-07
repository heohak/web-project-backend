package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.ProfileDTO;
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
    public void updateProfile(@RequestBody ProfileDTO dto) {
        profileService.updateProfile(dto);
    }

    @GetMapping("/random")
    public ProfileDTO getRandomProfile() {
        return profileService.getRandomProfile();
    }
}
