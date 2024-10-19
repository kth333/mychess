package com.g1.mychess.tournament.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.g1.mychess.tournament.service.*;
import com.g1.mychess.tournament.dto.*;
import com.g1.mychess.tournament.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    private final JwtUtil jwtUtil;

    public TournamentController(TournamentService tournamentService, JwtUtil jwtUtil) {
        this.tournamentService = tournamentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/admin/create")
    public ResponseEntity<TournamentDTO> createTournament(@Valid @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.createTournament(tournamentDTO, request);
    }

    @GetMapping("/public/get/{tournamentName}")
    public ResponseEntity<TournamentDTO> getTournamentByName(@Valid @PathVariable String tournamentName) {
        return tournamentService.findTournamentByName(tournamentName);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@Valid @PathVariable Long id) {
        return tournamentService.findTournamentById(id);
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @PutMapping("/admin/update")
    public ResponseEntity<TournamentDTO> updateTournament(@Valid @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.updateTournament(tournamentDTO, request);
    }

    @DeleteMapping("/player/leave/{tournamentId}")
    public ResponseEntity<String> leaveTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.leaveTournament(tournamentId, request);
    }

    @DeleteMapping("/admin/remove/{tournamentId}/{playerId}")
    public ResponseEntity<String> removePlayerFromTournament(@Valid @PathVariable Long tournamentId, @Valid @PathVariable Long playerId, HttpServletRequest request) {
        return tournamentService.removePlayerFromTournament(tournamentId, playerId, request);
    }

    @PostMapping("/admin/start/{tournamentId}")
    public ResponseEntity<String> startTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.startTournament(tournamentId, request);
    }

    @PostMapping("/admin/next-round/{tournamentId}")
    public ResponseEntity<String> prepareNextRound(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.prepareNextRound(tournamentId, request);
    }

    @PostMapping("/admin/complete/{tournamentId}")
    public ResponseEntity<String> completeTournament(@Valid @PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.completeTournament(tournamentId, request);
    }

    @PostMapping("/player/signup/{tournamentId}")
    public ResponseEntity<String> signUpToTournament(@Valid @PathVariable Long tournamentId, @Valid @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        Long playerId = jwtUtil.extractUserId(token);
        return tournamentService.signUpToTournament(tournamentId, playerId);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}