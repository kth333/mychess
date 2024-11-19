package com.g1.mychess.tournament.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.data.web.PageableDefault;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.g1.mychess.tournament.service.*;
import com.g1.mychess.tournament.dto.*;
import com.g1.mychess.tournament.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * REST controller for managing tournaments.
 */
@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;
    private final JwtUtil jwtUtil;

    /**
     * Constructs a new {@code TournamentController} with the specified services.
     *
     * @param tournamentService the service for managing tournaments
     * @param jwtUtil           the utility for handling JWT tokens
     */
    public TournamentController(TournamentService tournamentService, JwtUtil jwtUtil) {
        this.tournamentService = tournamentService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a new tournament.
     *
     * @param tournamentDTO the tournament details
     * @param request       the HTTP servlet request
     * @return a {@link ResponseEntity} containing the created tournament
     */
    @PostMapping("/admin")
    public ResponseEntity<TournamentDTO> createTournament(@Valid @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.createTournament(tournamentDTO, request);
    }

    /**
     * Retrieves a tournament by its name.
     *
     * @param tournamentName the name of the tournament
     * @return a {@link ResponseEntity} containing the tournament details
     */
    @GetMapping("/name/{tournamentName}")
    public ResponseEntity<TournamentDTO> getTournamentByName(@Valid @PathVariable String tournamentName) {
        return tournamentService.findTournamentByName(tournamentName);
    }

    /**
     * Retrieves a tournament by its ID.
     *
     * @param id the ID of the tournament
     * @return a {@link ResponseEntity} containing the tournament details
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@Valid @PathVariable Long id) {
        return tournamentService.findTournamentById(id);
    }

    /**
     * Retrieves all tournaments with pagination.
     *
     * @param pageable the pagination details
     * @return a {@link ResponseEntity} containing a page of tournaments
     */
    @GetMapping("/all")
    public ResponseEntity<Page<TournamentDTO>> getAllTournaments(@PageableDefault(size = 9) Pageable pageable) {
        return tournamentService.getAllTournaments(pageable);
    }

    /**
     * Retrieves upcoming tournaments with pagination.
     *
     * @param pageable the pagination details
     * @return a {@link ResponseEntity} containing a page of upcoming tournaments
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Page<TournamentDTO>> getUpcomingTournaments(@PageableDefault(size = 3) Pageable pageable) {
        return tournamentService.getUpcomingTournaments(pageable);
    }

    /**
     * Updates a tournament.
     *
     * @param tournamentDTO the updated tournament details
     * @param request       the HTTP servlet request
     * @return a {@link ResponseEntity} containing the updated tournament
     */
    @PutMapping("/admin")
    public ResponseEntity<TournamentDTO> updateTournament(@Valid @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.updateTournament(tournamentDTO, request);
    }

    /**
     * Allows a player to leave a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request      the HTTP servlet request
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @DeleteMapping("/player/{tournamentId}")
    public ResponseEntity<String> leaveTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.leaveTournament(tournamentId, request);
    }

    /**
     * Removes a player from a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player
     * @param request      the HTTP servlet request
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @DeleteMapping("/admin/{tournamentId}/players/{playerId}")
    public ResponseEntity<String> removePlayerFromTournament(@Valid @PathVariable Long tournamentId, @Valid @PathVariable Long playerId, HttpServletRequest request) {
        return tournamentService.removePlayerFromTournament(tournamentId, playerId, request);
    }

    /**
     * Starts a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request      the HTTP servlet request
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @PostMapping("/admin/{tournamentId}/status/in-progress")
    public ResponseEntity<String> startTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.startTournament(tournamentId, request);
    }

    /**
     * Prepares the next round of a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request      the HTTP servlet request
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @PostMapping("/admin/{tournamentId}/next-round")
    public ResponseEntity<String> prepareNextRound(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.prepareNextRound(tournamentId, request);
    }

    /**
     * Completes a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param request      the HTTP servlet request
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @PostMapping("/admin/{tournamentId}/status/completed")
    public ResponseEntity<String> completeTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.completeTournament(tournamentId, request);
    }

    /**
     * Signs up a player to a tournament.
     *
     * @param tournamentId        the ID of the tournament
     * @param authorizationHeader the authorization header containing the JWT token
     * @return a {@link ResponseEntity} containing the result of the operation
     */
    @PostMapping("/player/{tournamentId}")
    public ResponseEntity<String> signUpToTournament(@Valid @PathVariable Long tournamentId, @Valid @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        Long playerId = jwtUtil.extractUserId(token);
        return tournamentService.signUpToTournament(tournamentId, playerId);
    }

    /**
     * Retrieves the players participating in a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @return a {@link ResponseEntity} containing a list of players
     */
    @GetMapping("/{tournamentId}/players")
    public ResponseEntity<List<PlayerDTO>> getPlayersByTournament(@PathVariable Long tournamentId) {
        return tournamentService.getPlayersByTournament(tournamentId);
    }

    /**
     * Performs a health check for the service.
     *
     * @return a {@link ResponseEntity} indicating the service status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
