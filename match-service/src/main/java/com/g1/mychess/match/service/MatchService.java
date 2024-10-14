package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.model.Match;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {

    void runMatchmaking(Long tournamentId);

    void prepareNextRound(Long tournamentId);

    void finalizeTournament(Long tournamentId);

    ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw);

    TournamentDTO getTournamentDetails(Long tournamentId);

    List<MatchDTO> findAllMatchByTournament(Long tournamentId);

    List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber);
}