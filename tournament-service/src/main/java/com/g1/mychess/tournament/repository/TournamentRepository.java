package com.g1.mychess.tournament.repository;

import com.g1.mychess.tournament.model.Tournament;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>{
    Optional<Tournament> findByName(String name);
    Optional<Tournament> findById(Long id);
    List<Tournament> findByRegistrationStartDateTimeBeforeAndStatus(LocalDateTime dateTime, Tournament.TournamentStatus status);
}
