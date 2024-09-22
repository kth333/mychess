package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // Find profile by player id
    Optional<Profile> findByPlayer_PlayerId(Long playerId);
}
