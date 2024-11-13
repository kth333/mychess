package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for {@link MatchPlayer} entity.
 * This interface extends {@link JpaRepository} to provide CRUD operations for the {@link MatchPlayer} entity.
 * It also includes custom query methods for retrieving match player data based on player ID, match ID, tournament ID, and round number.
 */
@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {

    /**
     * Finds all {@link MatchPlayer} entities associated with a specific tournament and current round.
     * This method retrieves all match players for a given tournament ID and the current round.
     *
     * @param tournamentId The ID of the tournament to find match players for.
     * @param currentRound The round number to filter the match players by.
     * @return A list of {@link MatchPlayer} entities for the given tournament and current round.
     */
    List<MatchPlayer> findByMatch_TournamentIdAndCurrentRound(Long tournamentId, int currentRound);

    /**
     * Finds all {@link MatchPlayer} entities associated with a specific player and tournament.
     * This method retrieves all match players for a given player ID and tournament ID.
     *
     * @param playerId The ID of the player to find match players for.
     * @param tournamentId The ID of the tournament to filter the match players by.
     * @return A list of {@link MatchPlayer} entities for the given player and tournament.
     */
    List<MatchPlayer> findByPlayerIdAndMatch_TournamentId(Long playerId, Long tournamentId);

    /**
     * Finds a {@link MatchPlayer} entity by player ID and match ID.
     * This method retrieves a match player by the given player ID and match ID.
     *
     * @param playerId The ID of the player.
     * @param matchId The ID of the match.
     * @return A {@link MatchPlayer} entity for the given player and match ID.
     */
    MatchPlayer findByPlayerIdAndMatchId(Long playerId, Long matchId);

    /**
     * Finds a {@link MatchPlayer} entity by player ID, tournament ID, and current round.
     * This method retrieves a match player for a given player ID, tournament ID, and current round.
     *
     * @param playerId The ID of the player.
     * @param tournamentId The ID of the tournament.
     * @param currentRound The round number to filter the match player by.
     * @return A {@link MatchPlayer} entity for the given player, tournament, and current round.
     */
    MatchPlayer findByPlayerIdAndMatch_TournamentIdAndCurrentRound(Long playerId, Long tournamentId, int currentRound);

    /**
     * Finds all {@link MatchPlayer} entities associated with a specific match ID.
     * This method retrieves all match players for a given match ID.
     *
     * @param matchId The ID of the match to find match players for.
     * @return A list of {@link MatchPlayer} entities for the given match ID.
     */
    List<MatchPlayer> findByMatchId(Long matchId);

    List<MatchPlayer> findByMatch_TournamentIdOrderByPointsDesc(Long tournamentId);
}
