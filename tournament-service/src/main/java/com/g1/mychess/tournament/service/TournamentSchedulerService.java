package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.impl.TournamentServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TournamentSchedulerService {

    private final TournamentRepository tournamentRepository;
    private final TournamentServiceImpl tournamentService;

    public TournamentSchedulerService(TournamentRepository tournamentRepository, TournamentServiceImpl tournamentService) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentService = tournamentService;
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    @Transactional
    public void startScheduledTournaments() {
        LocalDateTime now = LocalDateTime.now();

        List<Tournament> tournamentsToStart = tournamentRepository.findByRegistrationStartDateTimeBeforeAndStatus(now, Tournament.TournamentStatus.UPCOMING);

        tournamentsToStart.forEach(tournament -> {
            try {
                tournamentService.startTournament(tournament.getId(), null); // Assuming internal call without HTTP request context
            } catch (Exception e) {
                // Handle errors (e.g., log an error message)
                System.err.println("Failed to start tournament with ID " + tournament.getId() + ": " + e.getMessage());
            }
        });
    }
}