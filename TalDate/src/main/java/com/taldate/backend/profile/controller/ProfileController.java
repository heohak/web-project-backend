package com.taldate.backend.profile.controller;

import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileDTO getCurrentProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        return profileService.getProfileById((int)(context.getAuthentication().getPrincipal()));
    }

//    @GetMapping
//    public List<ProfileDTO> getAllProfiles() {
//        return profileService.getAllProfiles();
//    }

//    @GetMapping("/{id}")
//    public ProfileDTO getProfileById(@PathVariable Integer id) {
//        return profileService.getProfileById(id);
//    }

    @PutMapping
    public void updateProfile(@RequestBody ProfileDTO profileDTO) {
        SecurityContext context = SecurityContextHolder.getContext();
        profileService.updateProfile((int)(context.getAuthentication().getPrincipal()), profileDTO);
    }

    @GetMapping("/random")
    public ProfileDTO getRandomProfile() {
        return profileService.getRandomProfile();
    }
}
