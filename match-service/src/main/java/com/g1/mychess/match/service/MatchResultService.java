package com.g1.mychess.match.service;

import org.springframework.http.ResponseEntity;

/**
 * Service interface for handling the completion of matches in the chess tournament.
 * Provides the method for finalizing a match and updating the results of the players involved.
 */
public interface MatchResultService {

    /**
     * Finalizes the match and updates the player results based on the outcome.
     *
     * @param matchId The ID of the match to complete.
     * @param winnerPlayerId The ID of the winning player (relevant only if the match is not a draw).
     * @param loserPlayerId The ID of the losing player (relevant only if the match is not a draw).
     * @param isDraw A flag indicating whether the match is a draw.
     * @return A {@link ResponseEntity} containing the result message of the operation.
     */
    ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw);
}
