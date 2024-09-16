package com.g1.mychess.tournament.repository;

import com.g1.mychess.tournament.model.TournamentPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {
    boolean existsByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
}