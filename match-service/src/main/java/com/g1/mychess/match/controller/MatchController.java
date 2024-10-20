package com.g1.mychess.match.controller;

import java.util.*;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.service.MatchService;
import com.g1.mychess.match.model.*;
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

    @PostMapping("/admin/matchmaking/{tournamentId}")
    public ResponseEntity<String> runMatchmaking(@PathVariable Long tournamentId, @Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        matchService.runMatchmaking(matchmakingDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Matchmaking for tournament " + tournamentId + " started successfully.");
    }

    @PostMapping("/admin/complete/{matchId}")
    public ResponseEntity<String> completeMatch(@PathVariable Long matchId,@Valid @RequestBody MatchResultDTO matchResultDTO) {
        return matchService.completeMatch(matchId, matchResultDTO.getWinnerId(), matchResultDTO.getLoserId(), matchResultDTO.isDraw());
    }

    @PostMapping("/admin/prepare-next-round/{tournamentId}")
    public ResponseEntity<String> prepareNextRound(@PathVariable Long tournamentId,@Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        matchService.prepareNextRound(matchmakingDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Next round for tournament " + tournamentId + " prepared successfully.");
    }

    @PostMapping("/admin/finalize/{tournamentId}")
    public ResponseEntity<String> finalizeTournament(@Valid @RequestBody MatchmakingDTO matchmakingDTO) {
        matchService.finalizeTournament(matchmakingDTO);
        return ResponseEntity.ok("Tournament finalized successfully.");
    }

    @GetMapping("/public/all/{tournamentId}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentId(@PathVariable Long tournamentId) {
        List<MatchDTO> matches = matchService.findAllMatchByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/public/all/{tournamentId}/round/{roundNumber}")
    public ResponseEntity<List<MatchDTO>> getAllMatchByTournamentIdAndRoundNum(@PathVariable Long tournamentId, @PathVariable Integer roundNumber) {
        List<MatchDTO> matches = matchService.findAllMatchByTournamentRound(tournamentId, roundNumber);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}