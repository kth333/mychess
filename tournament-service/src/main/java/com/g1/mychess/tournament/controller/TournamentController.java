package com.g1.mychess.tournament.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.g1.mychess.tournament.service.*;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.createTournament(tournamentDTO, request);
    }

    @GetMapping("/public/get/{tournamentName}")
    public ResponseEntity<TournamentDTO> getTournamentByName(@PathVariable String tournamentName) {
        return tournamentService.findTournamentByName(tournamentName);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Long id) {
        return tournamentService.findTournamentById(id);
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @PutMapping("/admin/update")
    public ResponseEntity<TournamentDTO> updateTournament(@RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        return tournamentService.updateTournament(tournamentDTO, request);
    }

    @PostMapping("/admin/start/{tournamentId}")
    public ResponseEntity<String> startTournament(@PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.startTournament(tournamentId, request);
    }

    @PostMapping("/admin/next-round/{tournamentId}")
    public ResponseEntity<String> prepareNextRound(@PathVariable Long tournamentId, HttpServletRequest request) {
        return tournamentService.prepareNextRound(tournamentId, request);
    }

    @PostMapping("/player/signup/{tournamentId}")
    public ResponseEntity<String> signUpToTournament(@PathVariable Long tournamentId, @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
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