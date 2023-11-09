package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO createdProfile = profileService.createProfile(profileDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProfileDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ProfileDTO getProfileById(@PathVariable Integer id) {
        return profileService.getProfileById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileById(@PathVariable Integer id) {
        profileService.deleteProfileById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/genderPreference")
    public ProfileDTO updateGenderPreference(@PathVariable Integer id, @RequestBody ProfileDTO profileDTO) {
        return profileService.updateGenderPreference(id, profileDTO);
    }

    @PutMapping("/{id}/bio")
    public ProfileDTO updateBio(@PathVariable Integer id, @RequestBody ProfileDTO profileDTO) {
        return profileService.updateBio(id, profileDTO);
    }

    @PutMapping("/{id}/picture")
    public ProfileDTO updatePicture(@PathVariable Integer id, @RequestBody ProfileDTO profileDTO) {
        return profileService.updatePicture(id, profileDTO);
    }
}
