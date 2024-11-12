package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.dto.TournamentResultsDTO;
import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {

    void runMatchmaking(MatchmakingDTO matchmakingDTO);

    void finalizeTournament(MatchmakingDTO matchmakingDTO);

    ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw);

    List<MatchDTO> findAllMatchByTournament(Long tournamentId);

    List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber);

    List<MatchResultDTO> findAllMatchResultsByTournament(Long tournamentId);

    ResponseEntity<String> updateMatchTime(Long matchId, UpdateMatchTimeDTO updateDTO, HttpServletRequest request);

    TournamentResultsDTO getTournamentResults(Long tournamentId);
}