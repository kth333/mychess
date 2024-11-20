package com.g1.mychess.tournament.repository;

import com.g1.mychess.tournament.model.TournamentPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing {@link TournamentPlayer} entities.
 * Provides CRUD operations and custom queries related to tournament players.
 */
public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {

    /**
     * Checks if a player is already registered for a specific tournament.
     *
     * @param tournamentId the tournament's ID
     * @param playerId the player's ID
     * @return true if the player is registered for the tournament, false otherwise
     */
    boolean existsByTournamentIdAndPlayerId(Long tournamentId, Long playerId);

    /**
     * Finds a {@link TournamentPlayer} by the tournament and player IDs.
     *
     * @param playerId the player's ID
     * @param tournamentId the tournament's ID
     * @return an {@link Optional} containing the {@link TournamentPlayer} if found, or an empty Optional
     */
    Optional<TournamentPlayer> findByTournamentIdAndPlayerId(Long playerId, Long tournamentId);

    /**
     * Finds all {@link TournamentPlayer} entries for a given tournament.
     *
     * @param tournamentId the tournament's ID
     * @return an {@link Optional} containing a list of {@link TournamentPlayer}s for the tournament,
     *         or an empty Optional if no players are found
     */
    Optional<List<TournamentPlayer>> findByTournamentId(Long tournamentId);
}
