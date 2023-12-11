package com.taldate.backend.match.service;

import com.taldate.backend.match.dto.MatchDTO;
import com.taldate.backend.match.entity.Match;
import com.taldate.backend.match.repository.MatchRepository;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.entity.Profile;
import com.taldate.backend.profile.mapper.ProfileMapper;
import com.taldate.backend.profile.repository.ProfileRepository;
import com.taldate.backend.profile.service.ProfileService;
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

    public void match(MatchDTO dto) {
        // Other user
        int otherId = dto.id();
        if (profileRepository.findById(otherId).isEmpty()) {
            // User supplied id does not exist...
            return;
        }

        // Current user
        int id = profileService.getCurrentProfile().getId();

        if (id == otherId) {
            // User supplied id is the same user's id...
            return;
        }

        Optional<Match> potentialMatch = matchRepository.findPotentialMatch(id, otherId);
        if (potentialMatch.isEmpty()) {
            // Create new match
            Match match = new Match();
            match.setProfile1(profileRepository.findById(id).get());
            match.setProfile2(profileRepository.findById(id).get());
            match.setMatchedByBoth(false);
            matchRepository.save(match);
        } else {
            // Update the old match
            Match match = potentialMatch.get();
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
                // something gone wrong
                continue;
            }

            matchedProfiles.add(profileMapper.profileToProfileDTO(otherProfile.get()));
        }

        return matchedProfiles;
    }
}
