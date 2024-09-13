package com.g1.mychess.tournament.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.g1.mychess.tournament.service.*;
import org.springframework.web.bind.annotation.RestController;
import com.g1.mychess.tournament.dto.*;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/admin/create-tournament")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO tournamentDTO) {
        return tournamentService.createTournament(tournamentDTO);
    }

    @GetMapping("/public/tournamentName/{tournamentName}")
    public ResponseEntity<TournamentDTO> getTournamentByName(@PathVariable String tournamentName) {
        return tournamentService.findTournamentByName(tournamentName);
    }

}
