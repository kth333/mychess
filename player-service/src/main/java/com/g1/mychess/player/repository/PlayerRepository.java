package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Player} entities.
 * Provides methods to query and interact with player data in the database.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Finds a {@link Player} by their username.
     *
     * @param username the username of the player.
     * @return an {@link Optional} containing the player if found, otherwise empty.
     */
    Optional<Player> findByUsername(String username);

    /**
     * Finds a {@link Player} by their email address.
     *
     * @param email the email of the player.
     * @return an {@link Optional} containing the player if found, otherwise empty.
     */
    Optional<Player> findByEmail(String email);

    /**
     * Finds a {@link Player} by their ID.
     *
     * @param playerId the ID of the player.
     * @return an {@link Optional} containing the player if found, otherwise empty.
     */
    Optional<Player> findById(Long playerId);

    /**
     * Finds players whose username contains the provided query string (case-insensitive).
     * This is useful for search functionality.
     *
     * @param query the search query to match against usernames.
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link Player} entities matching the search query.
     */
    Page<Player> findByUsernameContainingIgnoreCase(String query, Pageable pageable);
}
