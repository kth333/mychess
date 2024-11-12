package com.g1.mychess.match.controller;

import java.util.*;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.dto.TournamentResultsDTO;
import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import com.g1.mychess.match.service.MatchService;
import com.g1.mychess.match.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/admin/{tournamentId}/matchmaking")
    public ResponseEntity<String> runMatchmaking(@PathVariable Long tournamentId, @Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        matchService.runMatchmaking(matchmakingDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Matchmaking for tournament " + tournamentId + " started successfully.");
    }

    @PostMapping("/admin/{matchId}/status/completed/match")
    public ResponseEntity<String> completeMatch(@PathVariable Long matchId,@Valid @RequestBody MatchResultDTO matchResultDTO) {
        return matchService.completeMatch(matchId, matchResultDTO.getWinnerId(), matchResultDTO.getLoserId(), matchResultDTO.getIsDraw());
    }

    @PostMapping("/admin/{tournamentId}/status/completed")
    public ResponseEntity<String> finalizeTournament(@Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        matchService.finalizeTournament(matchmakingDTO);
        return ResponseEntity.ok("Tournament finalized successfully.");
    }

    @GetMapping("/all/{tournamentId}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentId(@PathVariable Long tournamentId) {
        List<MatchDTO> matches = matchService.findAllMatchByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{tournamentId}/round/{roundNumber}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentIdAndRoundNum(@PathVariable Long tournamentId, @PathVariable Integer roundNumber) {
        List<MatchDTO> matches = matchService.findAllMatchByTournamentRound(tournamentId, roundNumber);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/all/result/{tournamentId}")
    public ResponseEntity<List<MatchResultDTO>> getAllMatchResultByTournament(@PathVariable Long tournamentId) {
        List<MatchResultDTO> matches = matchService.findAllMatchResultsByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @PutMapping("/admin/{matchId}/scheduled-time")
    public ResponseEntity<String> updateMatchTime(
            @PathVariable Long matchId,
            @Valid @RequestBody UpdateMatchTimeDTO updateDTO,
            HttpServletRequest request) {
        return matchService.updateMatchTime(matchId, updateDTO, request);
    }

    @GetMapping("/tournament/{tournamentId}/results")
    public ResponseEntity<TournamentResultsDTO> getTournamentResults(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getTournamentResults(tournamentId));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}