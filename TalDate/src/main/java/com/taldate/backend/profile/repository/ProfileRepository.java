package com.taldate.backend.profile.repository;

import com.taldate.backend.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findByProfileActiveTrue();
}
