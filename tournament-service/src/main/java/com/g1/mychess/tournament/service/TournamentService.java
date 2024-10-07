package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.dto.TournamentDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TournamentService {

    ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    ResponseEntity<TournamentDTO> findTournamentByName(String name);

    ResponseEntity<TournamentDTO> findTournamentById(Long id);

    ResponseEntity<List<TournamentDTO>> getAllTournaments();

    ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId);

    ResponseEntity<String> startTournament(Long tournamentId, HttpServletRequest request);

    ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request);
}