package com.taldate.backend.match.repository;


import com.taldate.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MatchRepository extends JpaRepository<Match, Integer> {
}