package com.taldate.backend.user.controller;

import com.taldate.backend.user.dto.*;
import com.taldate.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PutMapping("/email")
    public void updateEmail(@RequestBody UpdateEmailDTO dto) {
        userService.updateEmail(dto);
    }

    @PutMapping("/password")
    public void updatePassword(@RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto);
    }

    @PutMapping("/firstName")
    public void updateFirstName(@RequestBody UpdateFirstNameDTO dto) {
        userService.updateFirstName(dto);
    }

    @PutMapping("/lastName")
    public void updateLastName(@RequestBody UpdateLastNameDTO dto) {
        userService.updateLastName(dto);
    }

    @PutMapping("/dateOfBirth")
    public void updateDateOfBirth(@RequestBody UpdateDateOfBirthDTO dto) {
        userService.updateDateOfBirth(dto);
    }

    @PutMapping("/gender")
    public void updateGender(@RequestBody UpdateGenderDTO dto) {
        userService.updateGender(dto);
    }

    @DeleteMapping
    public void deleteUser() {
        userService.deleteUser();
    }
}
