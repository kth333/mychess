package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing tournaments, including operations for creating, updating,
 * and retrieving tournaments, as well as managing player participation.
 */
public interface TournamentService {

    /**
     * Creates a new tournament.
     *
     * @param tournamentDTO the {@link TournamentDTO} containing tournament details
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing the created {@link TournamentDTO}
     */
    ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    /**
     * Finds a tournament by its name.
     *
     * @param name the name of the tournament
     * @return a {@link ResponseEntity} containing the {@link TournamentDTO} of the found tournament
     */
    ResponseEntity<TournamentDTO> findTournamentByName(String name);

    /**
     * Finds a tournament by its ID.
     *
     * @param id the ID of the tournament
     * @return a {@link ResponseEntity} containing the {@link TournamentDTO} of the found tournament
     */
    ResponseEntity<TournamentDTO> findTournamentById(Long id);

    /**
     * Retrieves all tournaments with pagination support.
     *
     * @param pageable the pagination information
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link TournamentDTO}s
     */
    ResponseEntity<Page<TournamentDTO>> getAllTournaments(Pageable pageable);

    /**
     * Retrieves upcoming tournaments with pagination support.
     *
     * @param pageable the pagination information
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link TournamentDTO}s for upcoming tournaments
     */
    ResponseEntity<Page<TournamentDTO>> getUpcomingTournaments(Pageable pageable);

    /**
     * Updates an existing tournament.
     *
     * @param tournamentDTO the {@link TournamentDTO} containing updated tournament details
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing the updated {@link TournamentDTO}
     */
    ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    /**
     * Signs up a player for a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param playerId the ID of the player
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId);

    /**
     * Allows a player to leave a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> leaveTournament(Long tournamentId, HttpServletRequest request);

    /**
     * Removes a player from a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param playerId the ID of the player to remove
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> removePlayerFromTournament(Long tournamentId, Long playerId, HttpServletRequest request);

    /**
     * Starts a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> startTournament(Long tournamentId, HttpServletRequest request);

    /**
     * Prepares for the next round of a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request);

    /**
     * Marks a tournament as completed.
     *
     * @param tournamentId the ID of the tournament
     * @param request the HTTP request, used for extracting user information
     * @return a {@link ResponseEntity} containing a success or failure message
     */
    ResponseEntity<String> completeTournament(Long tournamentId, HttpServletRequest request);

    /**
     * Retrieves a list of players participating in a given tournament.
     *
     * @param tournamentId the ID of the tournament
     * @return a {@link ResponseEntity} containing a list of {@link PlayerDTO}s for the players in the tournament
     */
    ResponseEntity<List<PlayerDTO>> getPlayersByTournament(Long tournamentId);
}
