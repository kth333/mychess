package com.g1.mychess.match.controller;

import java.util.*;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.dto.TournamentResultsDTO;
import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import com.g1.mychess.match.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing match-related operations in the chess tournament system.
 * Provides endpoints for matchmaking, match result processing, tournament finalization, match time updates, and match retrieval.
 */
@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchmakingService matchmakingService;
    private final MatchResultService matchResultService;
    private final MatchTimeService matchTimeService;
    private final TournamentFinalisationService tournamentFinalisationService;

    /**
     * Constructs the MatchController with the required services.
     *
     * @param matchService The service for managing match-related operations.
     * @param matchmakingService The service for running matchmaking.
     * @param matchResultService The service for processing match results.
     * @param matchTimeService The service for updating match times.
     * @param tournamentFinalisationService The service for finalizing tournaments.
     */
    public MatchController(MatchService matchService, MatchmakingService matchmakingService, MatchResultService matchResultService, MatchTimeService matchTimeService, TournamentFinalisationService tournamentFinalisationService) {
        this.matchService = matchService;
        this.matchmakingService = matchmakingService;
        this.matchResultService = matchResultService;
        this.matchTimeService = matchTimeService;
        this.tournamentFinalisationService = tournamentFinalisationService;
    }

    /**
     * Endpoint for running matchmaking for a specific tournament.
     *
     * @param tournamentId The ID of the tournament for which matchmaking will be run.
     * @param matchmakingDTO The matchmaking details, including tournament round and participants.
     * @return A response indicating the success of the matchmaking process.
     */
    @PostMapping("/admin/{tournamentId}/matchmaking")
    public ResponseEntity<String> runMatchmaking(@PathVariable Long tournamentId, @Valid @RequestBody MatchmakingDTO matchmakingDTO, @Valid @RequestBody String tournamentFormat) {
        matchmakingService.runMatchmaking(matchmakingDTO, tournamentFormat);
        return ResponseEntity.status(HttpStatus.OK).body("Matchmaking for tournament " + tournamentId + " started successfully.");
    }

    /**
     * Endpoint for completing a match and recording the result (winner, loser, draw).
     *
     * @param matchId The ID of the match to be completed.
     * @param matchResultDTO The result of the match, including winner, loser, and draw status.
     * @return A response indicating the completion of the match.
     */
    @PostMapping("/admin/{matchId}/status/completed/match")
    public ResponseEntity<String> completeMatch(@PathVariable Long matchId,@Valid @RequestBody MatchResultDTO matchResultDTO) {
        return matchResultService.completeMatch(matchId, matchResultDTO.getWinnerId(), matchResultDTO.getLoserId(), matchResultDTO.getIsDraw());
    }

    /**
     * Endpoint for finalizing a tournament by processing its matchmaking results.
     *
     * @param matchmakingDTO The details of the matchmaking, including participants and rounds.
     * @return A response indicating the successful finalization of the tournament.
     */
    @PostMapping("/admin/{tournamentId}/status/completed")
    public ResponseEntity<String> finalizeTournament(@Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        tournamentFinalisationService.finalizeTournament(matchmakingDTO);
        return ResponseEntity.ok("Tournament finalized successfully.");
    }

    /**
     * Endpoint to retrieve all matches for a specific tournament.
     *
     * @param tournamentId The ID of the tournament to retrieve matches for.
     * @return A list of all matches for the given tournament.
     */
    @GetMapping("/all/{tournamentId}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentId(@PathVariable Long tournamentId) {
        List<MatchDTO> matches = matchService.findAllMatchByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    /**
     * Endpoint to retrieve all matches for a specific tournament and round number.
     *
     * @param tournamentId The ID of the tournament.
     * @param roundNumber The round number to retrieve matches for.
     * @return A list of matches for the given tournament and round number.
     */
    @GetMapping("/{tournamentId}/round/{roundNumber}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentIdAndRoundNum(@PathVariable Long tournamentId, @PathVariable Integer roundNumber) {
        List<MatchDTO> matches = matchService.findAllMatchByTournamentRound(tournamentId, roundNumber);
        return ResponseEntity.ok(matches);
    }

    /**
     * Endpoint to retrieve all match results for a specific tournament.
     *
     * @param tournamentId The ID of the tournament to retrieve match results for.
     * @return A list of match results for the given tournament.
     */
    @GetMapping("/all/result/{tournamentId}")
    public ResponseEntity<List<MatchResultDTO>> getAllMatchResultByTournament(@PathVariable Long tournamentId) {
        List<MatchResultDTO> matches = matchService.findAllMatchResultsByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    /**
     * Endpoint for updating the scheduled time of a specific match.
     *
     * @param matchId The ID of the match whose time is to be updated.
     * @param updateDTO The new scheduled time details.
     * @param request The HTTP request object (to capture additional request context).
     * @return A response indicating the success or failure of the time update.
     */
    @PutMapping("/admin/{matchId}/scheduled-time")
    public ResponseEntity<String> updateMatchTime(
            @PathVariable Long matchId,
            @Valid @RequestBody UpdateMatchTimeDTO updateDTO,
            HttpServletRequest request) {
        return matchTimeService.updateMatchTime(matchId, updateDTO, request);
    }

    /**
     * Fetches the results of a tournament by its ID.
     *
     * @param tournamentId The ID of the tournament.
     * @return A ResponseEntity with the tournament results.
     */
    @GetMapping("/tournament/{tournamentId}/results")
    public ResponseEntity<TournamentResultsDTO> getTournamentResults(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getTournamentResults(tournamentId));
    }

    /**
     * Health check endpoint to verify that the service is up and running.
     *
     * @return A response indicating the service status.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}