package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public List<ProfileDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ProfileDTO getProfileById(@PathVariable Integer id) {
        return profileService.getProfileById(id);
    }

    @PutMapping("/genderPreference")
    public ProfileDTO updateGenderPreference(@RequestBody ProfileDTO profileDTO) {
        int id = 1;
        return profileService.updateGenderPreference(id, profileDTO);
    }

    @PutMapping("/bio")
    public ProfileDTO updateBio(@RequestBody ProfileDTO profileDTO) {
        // temporary
        int id = 1;
        return profileService.updateBio(id, profileDTO);
    }

    @PutMapping("/picture")
    public ProfileDTO updatePicture(@RequestBody ProfileDTO profileDTO) {
        // temporary
        int id = 1;
        return profileService.updatePicture(id, profileDTO);
    }
}
