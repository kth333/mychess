package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.TournamentResultsDTO;
import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service interface for handling match-related operations in the chess application.
 * Defines methods for retrieving matches, results, and match details by tournament.
 */
public interface MatchService {

    /**
     * Retrieves all matches associated with a given tournament.
     *
     * @param tournamentId The ID of the tournament.
     * @return A list of MatchDTO objects representing the matches in the tournament.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     */
    List<MatchDTO> findAllMatchByTournament(Long tournamentId);

    /**
     * Retrieves all matches associated with a specific round of a given tournament.
     * It checks whether the tournament exists and then fetches matches for the specified round.
     *
     * @param tournamentId The ID of the tournament.
     * @param roundNumber  The round number of the tournament.
     * @return A list of MatchDTO objects representing the matches in the specified round.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     * @throws TournamentRoundNotFoundException if the round number does not exist for the tournament.
     */
    List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber);

    /**
     * Retrieves all match results associated with a given tournament.
     * Builds a MatchResultDTO for each match containing information about the winner, loser, or draw.
     *
     * @param tournamentId The ID of the tournament.
     * @return A list of MatchResultDTO objects representing the results of matches in the tournament.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     */
    List<MatchResultDTO> findAllMatchResultsByTournament(Long tournamentId);

    /**
     * Retrieves the results of a specific tournament based on the provided tournament ID.
     *
     * @param tournamentId The ID of the tournament for which the results are to be fetched.
     * @return A {@link TournamentResultsDTO} containing the results of the specified tournament.
     */
    TournamentResultsDTO getTournamentResults(Long tournamentId);

    /**
     * Sends reminders for upcoming matches. This method is intended to notify participants about
     * their scheduled matches in the tournament, ensuring that they are informed in advance.
     */
    void sendMatchReminders();
}