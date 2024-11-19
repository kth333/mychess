package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for managing {@link Profile} entities.
 * Provides methods to query and interact with the profile data in the database.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Finds a {@link Profile} by the associated player's ID.
     *
     * @param playerId the ID of the player whose profile is to be fetched.
     * @return an {@link Optional} containing the profile if found, otherwise empty.
     */
    Optional<Profile> findByPlayer_PlayerId(Long playerId);

    /**
     * Finds the top 50 profiles ordered by their Glicko rating in descending order.
     *
     * @return a list of the top 50 profiles, sorted by Glicko rating in descending order.
     */
    List<Profile> findTop50ByOrderByGlickoRatingDesc();
}
