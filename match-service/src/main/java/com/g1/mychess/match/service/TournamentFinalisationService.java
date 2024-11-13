package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;

/**
 * Service interface for finalizing a tournament.
 * Validates tournament completion, and calculates player ratings based on match results.
 */
public interface TournamentFinalisationService {

    /**
     * Finalizes the tournament by validating completion,
     * and updating player ratings based on match results.
     *
     * @param matchmakingDTO The data transfer object containing tournament and matchmaking details.
     * @throws TournamentNotFoundException If the tournament with the specified ID is not found.
     * @throws IllegalStateException If the tournament is not completed or if matches are not finished.
     */
    void finalizeTournament(MatchmakingDTO matchmakingDTO);
}