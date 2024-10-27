package com.g1.mychess.tournament.initializer;

import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TimeControlSetting; // Ensure you import your TimeControlSetting class
import com.g1.mychess.tournament.model.Tournament.TournamentFormat;
import com.g1.mychess.tournament.model.Tournament.TournamentStatus;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.model.TournamentPlayer.TournamentPlayerStatus;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Autowired
    private TournamentPlayerRepository tournamentPlayerRepository;

    @Override
    public void run(String... args) {
        // Check if tournaments already exist to avoid duplicate entries
        if (tournamentRepository.count() == 0) {
            createTournaments();
            System.out.println("Tournament data initialized successfully!");
        } else {
            System.out.println("Tournaments already exist in the database.");
        }

    }

    private void createTournaments() {
        Tournament tournament1 = new Tournament();
        tournament1.setId((long) 1);
        tournament1.setAdminId(1L);
        tournament1.setName("Kings and Queens Championship 2025");
        tournament1.setDescription("A tournament celebrating diversity in chess");
        tournament1.setMaxPlayers(16);
        tournament1.setStartDateTime(LocalDateTime.of(2025, 5, 1, 10, 0));
        tournament1.setEndDateTime(LocalDateTime.of(2025, 5, 5, 18, 0));
        tournament1.setRegistrationStartDate(LocalDateTime.of(2025, 1, 1, 0, 0));
        tournament1.setRegistrationEndDate(LocalDateTime.of(2025, 4, 15, 23, 59));
        tournament1.setFormat(TournamentFormat.ROUND_ROBIN);
        tournament1.setTimeControlSetting(new TimeControlSetting(15, 10)); 
        tournament1.setStatus(TournamentStatus.UPCOMING);
        tournament1.setMinRating(1200);
        tournament1.setMaxRating(2000);
        tournament1.setAffectsRating(true);
        tournament1.setMinAge(16);
        tournament1.setMaxAge(60);
        tournament1.setRequiredGender(null);
        tournament1.setCountry("USA");
        tournament1.setRegion("California");
        tournament1.setCity("San Francisco");
        tournament1.setAddress("100 Chess Way");
        tournament1.setCurrentRound(0);
        tournament1.setMaxRounds(3);
        tournament1.setParticipants(new HashSet<>());

        Tournament tournament2 = new Tournament();
        tournament2.setId((long) 2);
        tournament2.setAdminId(2L);
        tournament2.setName("Women Grand Prix 2025");
        tournament2.setDescription("An exclusive tournament for female chess players");
        tournament2.setMaxPlayers(32);
        tournament2.setStartDateTime(LocalDateTime.of(2025, 6, 10, 9, 0));
        tournament2.setEndDateTime(LocalDateTime.of(2025, 6, 15, 17, 0));
        tournament2.setRegistrationStartDate(LocalDateTime.of(2025, 2, 1, 0, 0));
        tournament2.setRegistrationEndDate(LocalDateTime.of(2025, 6, 1, 23, 59));
        tournament2.setFormat(TournamentFormat.SWISS);
        tournament2.setTimeControlSetting(new TimeControlSetting(15, 10)); 
        tournament2.setStatus(TournamentStatus.UPCOMING);
        tournament2.setMinRating(1000);
        tournament2.setMaxRating(2200);
        tournament2.setAffectsRating(true);
        tournament2.setMinAge(12);
        tournament2.setMaxAge(50);
        tournament2.setRequiredGender("Female");
        tournament2.setCountry("Canada");
        tournament2.setRegion("British Columbia");
        tournament2.setCity("Vancouver");
        tournament2.setAddress("200 Chess Ave");
        tournament2.setCurrentRound(0);
        tournament2.setMaxRounds(7);
        tournament2.setParticipants(new HashSet<>());

        Tournament tournament3 = new Tournament();
        tournament3.setId((long) 3);
        tournament3.setAdminId(3L);
        tournament3.setName("Mixed Doubles Chess Open 2025");
        tournament3.setDescription("A unique tournament featuring mixed-gender teams");
        tournament3.setMaxPlayers(24);
        tournament3.setStartDateTime(LocalDateTime.of(2025, 7, 5, 10, 0));
        tournament3.setEndDateTime(LocalDateTime.of(2025, 7, 10, 18, 0));
        tournament3.setRegistrationStartDate(LocalDateTime.of(2025, 3, 1, 0, 0));
        tournament3.setRegistrationEndDate(LocalDateTime.of(2025, 6, 20, 23, 59));
        tournament3.setFormat(TournamentFormat.KNOCKOUT);
        tournament3.setTimeControlSetting(new TimeControlSetting(15, 10)); 
        tournament3.setStatus(TournamentStatus.UPCOMING);
        tournament3.setMinRating(1400);
        tournament3.setMaxRating(2500);
        tournament3.setAffectsRating(true);
        tournament3.setMinAge(18);
        tournament3.setMaxAge(70);
        tournament3.setRequiredGender(null);
        tournament3.setCountry("Australia");
        tournament3.setRegion("New South Wales");
        tournament3.setCity("Sydney");
        tournament3.setAddress("300 Chess Rd");
        tournament3.setCurrentRound(0);
        tournament3.setMaxRounds(6);
        tournament3.setParticipants(new HashSet<>());

        Tournament tournament4 = new Tournament();
        tournament4.setId((long) 4);
        tournament4.setAdminId(1L);
        tournament4.setName("Youth Chess Challenge 2025");
        tournament4.setDescription("A tournament for young aspiring chess players");
        tournament4.setMaxPlayers(16);
        tournament4.setStartDateTime(LocalDateTime.of(2025, 8, 15, 9, 0));
        tournament4.setEndDateTime(LocalDateTime.of(2025, 8, 20, 20, 0));
        tournament4.setRegistrationStartDate(LocalDateTime.of(2025, 4, 1, 0, 0));
        tournament4.setRegistrationEndDate(LocalDateTime.of(2025, 8, 5, 23, 59));
        tournament4.setFormat(TournamentFormat.ROUND_ROBIN);
        tournament4.setTimeControlSetting(new TimeControlSetting(15, 10)); 
        tournament4.setStatus(TournamentStatus.UPCOMING);
        tournament4.setMinRating(1600);
        tournament4.setMaxRating(3000);
        tournament4.setAffectsRating(true);
        tournament4.setMinAge(10);
        tournament4.setMaxAge(18);
        tournament4.setRequiredGender(null);
        tournament4.setCountry("India");
        tournament4.setRegion("Maharashtra");
        tournament4.setCity("Mumbai");
        tournament4.setAddress("400 Chess Park");
        tournament4.setCurrentRound(0);
        tournament4.setMaxRounds(5);
        tournament4.setParticipants(new HashSet<>());

        // Save the tournaments to the repository
        tournamentRepository.save(tournament1);
        tournamentRepository.save(tournament2);
        tournamentRepository.save(tournament3);
        tournamentRepository.save(tournament4);

        signUpPlayersForTournament(tournament1);
        System.out.println("Players signed up for tournament 1 successfully!");

    }

    private void signUpPlayersForTournament(Tournament tournament) {
        LocalDateTime signUpDateTime = LocalDateTime.now();

        for (int i = 1; i <= 8; i++) {
            TournamentPlayer tournamentPlayer = new TournamentPlayer();
            tournamentPlayer.setPlayerId((long) i);
            tournamentPlayer.setTournament(tournament);
            tournamentPlayer.setSignUpDateTime(signUpDateTime);
            tournamentPlayer.setGlickoRating(1500); // Hardcoded Glicko rating
            tournamentPlayer.setRatingDeviation(350);
            tournamentPlayer.setVolatility(0.06);
            tournamentPlayer.setPoints(0.0);
            tournamentPlayer.setRoundsPlayed(0);
            tournamentPlayer.setStatus(TournamentPlayerStatus.ACTIVE);

            // Save the tournament player
            tournamentPlayerRepository.save(tournamentPlayer);
        }
    }
}
