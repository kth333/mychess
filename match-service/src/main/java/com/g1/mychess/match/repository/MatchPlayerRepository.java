package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {

    // Method to find all MatchPlayers by tournamentId
    List<MatchPlayer> findByMatch_TournamentId(Long tournamentId);
    Optional<MatchPlayer> findByPlayerIdAndCurrentRound(Long playerId, int roundNumber);

}
