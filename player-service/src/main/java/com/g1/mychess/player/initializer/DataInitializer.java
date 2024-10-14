package com.g1.mychess.player.initializer;

import com.g1.mychess.player.model.CustomChessRank;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        dropDatabase();
        // Check if players already exist to avoid duplicate entries
        if (playerRepository.count() == 0) {
            initializePlayers();
            System.out.println("Player accounts initialized.");
        } else {
            System.out.println("Player accounts already exist in the database.");
        }
    }

    private void dropDatabase() {
        // Adjust the SQL command according to your database
        String dropSQL = "DROP DATABASE PLAYER_SERVICE_DB;";
        try {
            jdbcTemplate.execute(dropSQL);
            System.out.println("Database dropped successfully.");
        } catch (Exception e) {
            System.err.println("Failed to drop database: " + e.getMessage());
        }
    }

    private void initializePlayers() {
        List<Player> players = new ArrayList<>();
        List<Profile> profiles = new ArrayList<>();

        // Creating 8 players with profiles
        for (int i = 1; i <= 8; i++) {
            Player player = new Player();
            player.setUsername("player" + i);
            player.setPassword("$2a$10$/1b1aYbc9OGxNxDsuf4DJOe9QlW33cl7NAn0B.Rtd0gpthO6f2bnq"); // Set hashed password
            player.setEmail("player" + i + "@example.com");
            player.setJoinedDate(LocalDate.now());
            player.setTournamentCount(0); // Starting with 0 tournaments
            player.setRatingHistory(new ArrayList<>());

            // Create and set profile for each player
            Profile profile = new Profile();
            profile.setPlayer(player);
            profile.setFullName("Player " + i);
            profile.setBio("This is player " + i);
            profile.setAvatarUrl(null);
            profile.setCountry("Country " + i);
            profile.setCity("City " + i);
            profile.setBirthDate(LocalDate.of(1990 + i, 1, 1)); // Vary birth dates
            profile.setRank(CustomChessRank.INTERMEDIATE); // Assuming PLAYER rank for all initially
            profile.setGlickoRating(1500); // Vary Glicko rating between 1450 and 1550
            profile.setRatingDeviation(350.0);
            profile.setVolatility(0.06);
            profile.setTotalWins(0);
            profile.setTotalLosses(0);
            profile.setTotalDraws(0);
            profile.setLastActive(LocalDate.now()); 
            profile.setPublic(true); // Setting all profiles to public for now

            player.setProfile(profile);
            players.add(player);
            profiles.add(profile);

            System.out.println("Player initialized with username: " + player.getUsername());
        }

        // Save all players and their profiles to the database
        profileRepository.saveAll(profiles);
        playerRepository.saveAll(players);
    }
}
