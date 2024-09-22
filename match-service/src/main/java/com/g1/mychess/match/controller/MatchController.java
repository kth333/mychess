package com.g1.mychess.match.controller;

import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.service.MatchService;
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
    public ResponseEntity<String> runMatchmaking(@PathVariable Long tournamentId) {
        matchService.runMatchmaking(tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body("Matchmaking for tournament " + tournamentId + " started successfully.");
    }

    @PostMapping("/admin/complete/{matchId}")
    public ResponseEntity<String> completeMatch(@PathVariable Long matchId, @RequestBody MatchResultDTO matchResultDTO) {
        return matchService.completeMatch(matchId, matchResultDTO.getWinnerId(), matchResultDTO.getLoserId(), matchResultDTO.isDraw());
    }

    @PostMapping("/admin/prepare-next-round/{tournamentId}")
    public ResponseEntity<String> prepareNextRound(@PathVariable Long tournamentId) {
        matchService.prepareNextRound(tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body("Next round for tournament " + tournamentId + " prepared successfully.");
    }
}