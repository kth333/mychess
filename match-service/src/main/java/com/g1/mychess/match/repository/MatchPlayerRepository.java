package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {

    // Method to find all MatchPlayers by tournamentId
    List<MatchPlayer> findByMatch_TournamentIdAndCurrentRound(Long tournamentId, int currentRound);

    List<MatchPlayer> findByPlayerIdAndMatch_TournamentId(Long playerId, Long tournamentId);

    MatchPlayer findByPlayerIdAndMatchId(Long playerId, Long matchId);

    MatchPlayer findByPlayerIdAndMatch_TournamentIdAndCurrentRound(Long playerId, Long tournamentId, int currentRound);
}
