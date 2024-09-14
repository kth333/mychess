package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUsername(String username);

    Optional<Player> findByEmail(String email);

    Optional<Player> findById(Long playerId);

    // Check if a player already exists by email
    boolean existsByEmail(String email);

    // Check if a player already exists by username
    boolean existsByUsername(String username);


}
