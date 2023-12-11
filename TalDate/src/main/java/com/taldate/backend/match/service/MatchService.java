package com.taldate.backend.match.service;

import com.taldate.backend.match.dto.MatchDTO;
import com.taldate.backend.match.entity.Match;
import com.taldate.backend.match.repository.MatchRepository;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.mapper.ProfileMapper;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.profile.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private final ProfileRepository profileRepository;
    private final MatchRepository matchRepository;
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @Transactional
    public void match(MatchDTO dto) {
        // Other user
        int otherId = dto.id();
        if (profileRepository.findById(otherId).isEmpty()) {
            log.warn("user supplied id does not exist");
            return;
        }

        // Current user
        int id = profileService.getCurrentProfile().getId();

        if (id == otherId) {
            log.warn("user supplied id is the same as user's own id");
            return;
        }

        Optional<Match> potentialMatch = matchRepository.findPotentialMatch(id, otherId);
        if (potentialMatch.isEmpty()) {
            // Create new match
            Match match = new Match();
            match.setProfile1(profileService.getCurrentProfile());  // profile1 is always the first matcher
            match.setProfile2(profileRepository.findById(otherId).get());
            match.setMatchedByBoth(false);
            matchRepository.save(match);
        } else {
            Match match = potentialMatch.get();
            if (match.getProfile1().getId() == id) {
                // We are matching again with the same
                return;
            }
            // Update the old match
            match.setMatchedByBoth(true);
            matchRepository.save(match);
        }
    }

    public List<ProfileDTO> getAllMatches() {
        int id = profileService.getCurrentProfile().getId();

        List<Match> matches = matchRepository.findAllPositiveMatches(id);
        List<ProfileDTO> matchedProfiles = new ArrayList<>();
        for (Match match : matches) {
            int otherId = match.getProfile1().getId() == id ?
                    match.getProfile2().getId() :
                    match.getProfile1().getId();

            Optional<Profile> otherProfile = profileRepository.findById(otherId);
            if (otherProfile.isEmpty()) {
                log.error("match exists with a non-existent profile");
                continue;
            }

            matchedProfiles.add(profileMapper.profileToProfileDTO(otherProfile.get()));
        }

        return matchedProfiles;
    }
}
