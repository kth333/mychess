package com.g1.mychess.player.initializer;

import com.g1.mychess.player.model.CustomChessRank;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PlayerRatingHistoryRepository playerRatingHistoryRepository;

    @Override
    public void run(String... args) {
        if (playerRepository.count() == 0) {
            initializePlayers();
            System.out.println("Player accounts initialized.");
        } else {
            System.out.println("Player accounts already exist in the database.");
        }
    }

    private void initializePlayers() {
        List<Player> players = new ArrayList<>();
        List<Profile> profiles = new ArrayList<>();
        List<PlayerRatingHistory> playerRatingHistories = new ArrayList<>();
        List<String> genders = Arrays.asList("MALE", "FEMALE", "OTHERS");
        Random random = new Random();

        // Define top 10 players with actual countries and high ratings (e.g., based on FIDE ratings)
        List<String> topPlayerNames = Arrays.asList(
                "Magnus Carlsen", "Neo Wei Yi", "Ding Liren", "Alireza Firouzja",
                "Fabiano Caruana", "Levon Aronian", "Anish Giri", "Wesley So",
                "Hikaru Nakamura", "Richard Rapport"
        );

        List<String> topPlayerCountries = Arrays.asList(
                "Norway", "Russia", "China", "France",
                "USA", "Armenia", "Netherlands", "USA",
                "USA", "Hungary"
        );

        List<Integer> topPlayerRatings = Arrays.asList(
                2850, 2785, 2805, 2793,
                2780, 2764, 2760, 2755,
                2745, 2735
        );

        // Creating 8 regular players
        for (int i = 1; i <= 8; i++) {
            Player player = createRegularPlayer(i, genders, random);
            players.add(player);
            profiles.add(player.getProfile());
            playerRatingHistories.add(createRatingHistory(player));
        }

        // Creating 50 grandmaster players
        for (int i = 0; i < 50; i++) {
            Player player;
            if (i < 10) {
                // Top 10 Grandmasters with specific details
                player = createGrandmaster(
                        topPlayerNames.get(i), topPlayerNames.get(i).replaceAll("\\s", ""),
                        topPlayerCountries.get(i), topPlayerRatings.get(i),
                        genders.get(random.nextInt(genders.size())), random
                );
            } else {
                // Other 40 Grandmasters with random names and high ratings
                player = createGrandmaster(
                        "Grandmaster " + (i + 11), "grandmaster" + (i + 1),
                        "Country " + (i + 1), 2650 + random.nextInt(101), // Rating between 2650 - 2750
                        genders.get(random.nextInt(genders.size())), random
                );
            }

            players.add(player);
            profiles.add(player.getProfile());
            playerRatingHistories.add(createRatingHistory(player));
            System.out.println("Grandmaster initialized with username: " + player.getUsername());
        }

        // Save all players and profiles
        profileRepository.saveAll(profiles);
        playerRepository.saveAll(players);
        playerRatingHistoryRepository.saveAll(playerRatingHistories);
    }

    private Player createRegularPlayer(int index, List<String> genders, Random random) {
        Player player = new Player();
        player.setUsername("player" + index);
        player.setPassword("$2a$10$/1b1aYbc9OGxNxDsuf4DJOe9QlW33cl7NAn0B.Rtd0gpthO6f2bnq");
        player.setEmail("player" + index + "@example.com");
        player.setJoinedDate(LocalDate.now());
        player.setTournamentCount(0);
        player.setRatingHistory(new ArrayList<>());

        Profile profile = new Profile();
        profile.setPlayer(player);
        profile.setFullName("Player " + index);
        profile.setBio("This is player " + index);
        profile.setAvatarUrl("https://static.vecteezy.com/system/resources/previews/022/763/685/non_2x/the-figure-of-a-horse-in-chess-icon-vector.jpg");
        profile.setGender(genders.get(random.nextInt(genders.size())));
        profile.setCountry("Country " + index);
        profile.setRegion("Region " + index);
        profile.setCity("City " + index);
        profile.setBirthDate(LocalDate.of(1990 + index, 1, 1));
        profile.setRank(CustomChessRank.INTERMEDIATE);
        profile.setGlickoRating(1450 + random.nextInt(101));
        profile.setRatingDeviation(350.0);
        profile.setVolatility(0.06);
        profile.setTotalWins(0);
        profile.setTotalLosses(0);
        profile.setTotalDraws(0);
        profile.setLastActive(LocalDate.now());
        profile.setPublic(true);

        player.setProfile(profile);
        return player;
    }

    private Player createGrandmaster(String fullName, String username, String country, int glickoRating, String gender, Random random) {
        Player player = new Player();
        player.setUsername(username);
        player.setPassword("$2a$10$/1b1aYbc9OGxNxDsuf4DJOe9QlW33cl7NAn0B.Rtd0gpthO6f2bnq");
        player.setEmail(username + "@example.com");
        player.setJoinedDate(LocalDate.now());
        player.setTournamentCount(0);
        player.setRatingHistory(new ArrayList<>());

        Profile profile = new Profile();
        profile.setPlayer(player);
        profile.setFullName(fullName);
        profile.setBio("This is grandmaster " + fullName);
        profile.setAvatarUrl("https://static.vecteezy.com/system/resources/previews/022/763/685/non_2x/the-figure-of-a-horse-in-chess-icon-vector.jpg");
        profile.setGender(gender);
        profile.setCountry(country);
        profile.setRegion("Region " + random.nextInt(5));
        profile.setCity("City " + random.nextInt(5));
        profile.setBirthDate(LocalDate.of(1980 + random.nextInt(15), 1, 1)); // Vary birth dates
        profile.setRank(CustomChessRank.GRANDMASTER); // Set GRANDMASTER rank
        profile.setGlickoRating(glickoRating); // Set high Glicko rating
        profile.setRatingDeviation(100.0); // Set lower deviation for experienced players
        profile.setVolatility(0.04); // Slightly lower volatility
        profile.setTotalWins(random.nextInt(100));
        profile.setTotalLosses(random.nextInt(50));
        profile.setTotalDraws(random.nextInt(25));
        profile.setLastActive(LocalDate.now());
        profile.setPublic(true);

        player.setProfile(profile);
        return player;
    }

    private PlayerRatingHistory createRatingHistory(Player player) {
        PlayerRatingHistory playerRatingHistory = new PlayerRatingHistory();
        playerRatingHistory.setPlayer(player);
        playerRatingHistory.setGlickoRating(player.getProfile().getGlickoRating());
        playerRatingHistory.setRatingDeviation(player.getProfile().getRatingDeviation());
        playerRatingHistory.setVolatility(player.getProfile().getVolatility());
        playerRatingHistory.setDate(LocalDateTime.now());
        return playerRatingHistory;
    }
}
