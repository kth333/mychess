package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchmakingDTO;

/**
 * MatchmakingService is an interface that defines the contract for implementing
 * matchmaking functionality in a chess tournament system. The service is responsible
 * for organizing players, pairing them for matches, and scheduling rounds in a tournament.
 */
public interface MatchmakingService {

    /**
     * Runs the matchmaking process for a given tournament.
     *
     * This method takes a MatchmakingDTO which contains the necessary data to perform
     * the matchmaking for a specific round of the tournament. It is expected to pair
     * players based on their performance (e.g., using a Swiss system), create matches,
     * and save the results in the database.
     *
     * @param matchmakingDTO The data transfer object containing tournament details,
     *                       the current round, and the list of participants.
     */
    void runMatchmaking(MatchmakingDTO matchmakingDTO, String tournamentFormat);
}
