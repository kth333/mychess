package com.g1.mychess.user.repository;

import com.g1.mychess.user.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Check if a player already exists by email
    boolean existsByEmail(String email);

    // Check if a player already exists by username
    boolean existsByUsername(String username);
}
