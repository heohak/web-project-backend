package com.taldate.backend.match.repository;


import com.taldate.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("SELECT m FROM Match m WHERE (m.profile1.id = :myId AND m.profile2.id = :otherId) OR (m.profile1.id = :otherId AND m.profile2.id = :myId)")
    Optional<Match> findPotentialMatch(int myId, int otherId);

    @Query("SELECT m FROM Match m WHERE m.matchedByBoth = TRUE AND (m.profile1.id = :myId OR m.profile2.id = :myId)")
    List<Match> findAllPositiveMatches(int myId);

    @Modifying
    @Query("DELETE FROM Match m WHERE m.profile1.id = ?1 OR m.profile2.id = ?1")
    void deleteMatchesByProfileId(int profileId);
}