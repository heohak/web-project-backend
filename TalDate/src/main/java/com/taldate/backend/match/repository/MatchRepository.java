package com.taldate.backend.match.repository;


import com.taldate.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("SELECT m FROM Match m WHERE (m.profile1.id = :myId AND m.profile2.id = :otherId) OR (m.profile1.id = :otherId AND m.profile2.id = :myId)")
    Optional<Match> findPotentialMatch(int myId, int otherId);
}