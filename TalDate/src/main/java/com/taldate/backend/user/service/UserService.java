package com.taldate.backend.user.service;

import com.taldate.backend.exception.ApplicationException;
import com.taldate.backend.match.repository.MatchRepository;
import com.taldate.backend.picture.PictureRepository;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.profile.service.ProfileService;
import com.taldate.backend.user.dto.*;
import com.taldate.backend.user.entity.User;
import com.taldate.backend.user.mapper.UserMapper;
import com.taldate.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final MatchRepository matchRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        int id = (int) (context.getAuthentication().getPrincipal());
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found while in service layer");
                    return new ApplicationException("User not found");
                });
    }

    public Page<UserDTO> getUsers(int page, int size, String sortBy, String sortDir, String search) {
        PageRequest pageable = createPageRequest(page, size, sortBy, sortDir);

        if (search != null && !search.trim().isEmpty()) {
            return searchUsers(search, pageable);
        } else {
            return findAllUsers(pageable);
        }
    }

    private Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDTO);
    }

    private Page<UserDTO> searchUsers(String search, Pageable pageable) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        search, search, search, pageable)
                .map(userMapper::userToUserDTO);
    }

    private PageRequest createPageRequest(int page, int size, String sortBy, String sortDir) {
        if (!"none".equalsIgnoreCase(sortDir)) {
            Sort.Direction direction = Sort.Direction.fromString(sortDir.toUpperCase());
            return PageRequest.of(page, size, direction, sortBy);
        } else {
            return PageRequest.of(page, size);
        }
    }

    public List<UserDTO> getAllUsers() {
        log.info("Retrieving all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    @Transactional
    public void updateEmail(UpdateEmailDTO dto) {
        User user = getCurrentUser();
        log.info("Updating email for user with ID: {}", user.getId());
        user.setEmail(dto.newEmail());
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO dto) {
        User user = getCurrentUser();
        log.info("Updating password for user with ID: {}", user.getId());
        user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser() {
        User user = getCurrentUser();
        Profile profile = profileService.getCurrentProfile();
        log.info("Deleting user with ID: {}", user.getId());

        // could be done on database level (cascading delete), but easier/foolproof for now
        pictureRepository.delete(profile.getPicture());
        matchRepository.deleteMatchesByProfileId(profile.getId());
        userRepository.delete(user);
        profileRepository.delete(profileService.getCurrentProfile());
    }

    @Transactional
    public void updateFirstName(UpdateFirstNameDTO dto) {
        User user = getCurrentUser();
        log.info("Updating first name for user with ID: {}", user.getId());
        user.setFirstName(dto.newFirstName());

        Profile profile = user.getProfile();
        profile.setName(profileService.combineFullName(user.getFirstName(), user.getLastName()));
        profileRepository.save(profile);

        userRepository.save(user);
    }

    @Transactional
    public void updateLastName(UpdateLastNameDTO dto) {
        User user = getCurrentUser();
        log.info("Updating last name for user with ID: {}", user.getId());
        user.setLastName(dto.newLastName());

        Profile profile = user.getProfile();
        profile.setName(profileService.combineFullName(user.getFirstName(), user.getLastName()));
        profileRepository.save(profile);

        userRepository.save(user);
    }

    @Transactional
    public void updateDateOfBirth(UpdateDateOfBirthDTO dto) {
        User user = getCurrentUser();
        log.info("Updating date of birth for user with ID: {}", user.getId());
        user.setDateOfBirth(dto.newDateOfBirth());

        Profile profile = user.getProfile();
        profile.setAge(profileService.calculateAge(dto.newDateOfBirth()));
        profileRepository.save(profile);

        userRepository.save(user);
    }

    @Transactional
    public void updateGender(UpdateGenderDTO dto) {
        User user = getCurrentUser();
        log.info("Updating gender for user with ID: {}", user.getId());
        user.setGenderMale(dto.genderMale());
        userRepository.save(user);
    }
}
