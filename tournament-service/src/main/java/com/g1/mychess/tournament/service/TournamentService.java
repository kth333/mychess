package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TournamentService {

    ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    ResponseEntity<TournamentDTO> findTournamentByName(String name);

    ResponseEntity<TournamentDTO> findTournamentById(Long id);

    ResponseEntity<Page<TournamentDTO>> getAllTournaments(Pageable pageable);

    ResponseEntity<Page<TournamentDTO>> getUpcomingTournaments(Pageable pageable);

    ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request);

    ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId);

    ResponseEntity<String> leaveTournament(Long tournamentId, HttpServletRequest request);

    ResponseEntity<String> removePlayerFromTournament(Long tournamentId, Long playerId, HttpServletRequest request);

    ResponseEntity<String> startTournament(Long tournamentId, HttpServletRequest request);

    ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request);

    ResponseEntity<String> completeTournament(Long tournamentId, HttpServletRequest request);

    ResponseEntity<List<PlayerDTO>> getPlayersByTournament(Long tournamentId);
}