package com.g1.mychess.tournament.repository;

import com.g1.mychess.tournament.model.TournamentPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {
    boolean existsByTournamentIdAndPlayerId(Long tournamentId, Long playerId);

    Optional<TournamentPlayer> findByTournamentIdAndPlayerId(Long playerId, Long tournamentId);
}