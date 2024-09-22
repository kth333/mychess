package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    public MatchService(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, WebClient.Builder webClientBuilder) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Transactional
    public ResponseEntity<String> completeMatch(Long matchId, MatchResultDTO matchResultDTO) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // Determine winner and loser
        if (matchResultDTO.isDraw()) {
            match.getParticipants().forEach(player -> player.setResult(MatchPlayer.Result.DRAW));
        } else {
            match.getParticipants().forEach(player -> {
                if (player.getPlayerId().equals(matchResultDTO.getWinnerId())) {
                    player.setResult(MatchPlayer.Result.WIN);
                } else {
                    player.setResult(MatchPlayer.Result.LOSS);
                }
            });
        }

        // Calculate new ratings and update match status
        match.getParticipants().forEach(this::calculateAndUpdatePlayerRatings);

        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);

        return ResponseEntity.status(HttpStatus.OK).body("Match completed and player ratings updated.");
    }

    private void calculateAndUpdatePlayerRatings(MatchPlayer matchPlayer) {
        // Placeholder for rating calculation logic
        int newGlickoRating = calculateNewRating(matchPlayer.getInitialRating(), matchPlayer.getResult());
        double newRatingDeviation = calculateNewRatingDeviation(matchPlayer.getInitialRatingDeviation(), matchPlayer.getResult()); // Implement based on Glicko-2 or other rating system
        double newVolatility = calculateNewVolatility(matchPlayer.getInitialVolatility(), matchPlayer.getResult()); // Implement based on Glicko-2 or other rating system

        // Update the matchPlayer with new values
        matchPlayer.setNewRating(newGlickoRating);
        matchPlayer.setNewRatingDeviation(newRatingDeviation);
        matchPlayer.setNewVolatility(newVolatility);

        // Update the player's profile and rating history in PlayerService
        PlayerRatingUpdateDTO ratingUpdate = new PlayerRatingUpdateDTO(
                matchPlayer.getPlayerId(),
                newGlickoRating,
                newRatingDeviation,
                newVolatility,
                matchPlayer.getMatch().getTournamentId(),
                matchPlayer.getResult().name()
        );

        updatePlayerProfileAndRatingHistory(ratingUpdate);
    }

    private int calculateNewRating(int initialRating, MatchPlayer.Result result) {
        // Implement rating calculation logic here based on the result
        return initialRating + (result == MatchPlayer.Result.WIN ? 10 : result == MatchPlayer.Result.LOSS ? -10 : 0);
    }

    private double calculateNewRatingDeviation(double initialRatingDeviation, MatchPlayer.Result result) {
        // Implement rating deviation calculation logic here
        // This is a placeholder
        return initialRatingDeviation * 0.9; // Example logic: Reduce deviation as more matches are played
    }

    private double calculateNewVolatility(double initialVolatility, MatchPlayer.Result result) {
        // Implement volatility calculation logic here
        // This is a placeholder
        return initialVolatility * 0.95; // Example logic: Adjust volatility slightly after a match
    }

    private void updatePlayerProfileAndRatingHistory(PlayerRatingUpdateDTO ratingUpdate) {
        webClientBuilder.build()
                .post()
                .uri(playerServiceUrl + "/api/v1/player/update-rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Transactional
    public void runMatchmaking(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        List<MatchPlayer> players = getOrCreateTournamentPlayers(tournament);

        int currentRound = matchRepository.findMaxRoundNumberByTournamentId(tournamentId).orElse(0) + 1;
        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(newMatches);
    }

    private List<MatchPlayer> getOrCreateTournamentPlayers(TournamentDTO tournament) {
        List<MatchPlayer> existingPlayers = matchPlayerRepository.findByMatch_TournamentId(tournament.getId());

        if (existingPlayers.isEmpty()) {
            return tournament.getParticipants().stream()
                    .map(participant -> {
                        MatchPlayer player = new MatchPlayer();
                        player.setPlayerId(participant.getPlayerId());
                        player.setInitialRating(participant.getGlickoRating());
                        player.setCurrentRound(1);
                        player.setPoints(0);
                        return player;
                    })
                    .collect(Collectors.toList());
        } else {
            return existingPlayers;
        }
    }

    private List<Match> createSwissSystemMatches(List<MatchPlayer> players, Long tournamentId, int roundNumber) {
        List<Match> matches = new ArrayList<>();
        List<MatchPlayer> updatedPlayers = new ArrayList<>();  // List to collect updated MatchPlayer entities

        // Sort players by their Glicko rating (descending) and points (highest to lowest)
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints).reversed()
                .thenComparing(MatchPlayer::getInitialRating).reversed());

        Set<Long> pairedPlayers = new HashSet<>(); // To track already paired players in this round

        for (int i = 0; i < players.size() - 1; i += 2) {
            MatchPlayer player1 = players.get(i);
            MatchPlayer player2 = players.get(i + 1);

            // Skip players who have already been paired in this round
            if (pairedPlayers.contains(player1.getPlayerId()) || pairedPlayers.contains(player2.getPlayerId())) {
                continue;
            }

            Match match = new Match();
            match.setTournamentId(tournamentId);
            match.setScheduledTime(LocalDateTime.now().plusDays(1));
            match.setStatus(Match.MatchStatus.SCHEDULED);
            match.setRoundNumber(roundNumber);

            // Save the match first to generate its ID
            match = matchRepository.save(match);

            // Update the current round for the players
            player1.setCurrentRound(roundNumber);
            player2.setCurrentRound(roundNumber);

            // Associate players with the match
            player1.setMatch(match);
            player2.setMatch(match);

            updatedPlayers.add(player1);
            updatedPlayers.add(player2);
            matches.add(match);

            pairedPlayers.add(player1.getPlayerId());
            pairedPlayers.add(player2.getPlayerId());
        }

        // Handle odd number of players by assigning a bye
        if (players.size() % 2 != 0 && !pairedPlayers.contains(players.get(players.size() - 1).getPlayerId())) {
            MatchPlayer byePlayer = players.get(players.size() - 1);
            byePlayer.setCurrentRound(roundNumber);  // Increment the round for the bye player
            assignByeToPlayer(byePlayer, tournamentId);

            updatedPlayers.add(byePlayer);
        }

        // Save all updated MatchPlayer entities
        matchPlayerRepository.saveAll(updatedPlayers);

        return matches;
    }


    private void assignByeToPlayer(MatchPlayer byePlayer, Long tournamentId) {
        byePlayer.setPoints(byePlayer.getPoints() + 1);

        Match byeMatch = new Match();
        byeMatch.setTournamentId(tournamentId);
        byeMatch.setScheduledTime(LocalDateTime.now().plusDays(1));
        byeMatch.setStatus(Match.MatchStatus.COMPLETED);
        byeMatch.setRoundNumber(byePlayer.getCurrentRound());

        byePlayer.setMatch(byeMatch);

        matchRepository.save(byeMatch);
        matchPlayerRepository.save(byePlayer);
    }

    @Transactional
    public void prepareNextRound(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);
        List<MatchPlayer> players = getOrCreateTournamentPlayers(tournament);

        int currentRound = determineCurrentRound(tournamentId) + 1;

        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints).reversed()
                .thenComparing(MatchPlayer::getInitialRating).reversed());

        if (isTournamentOver(tournament)) {
            finalizeTournament(tournamentId);
            return;
        }

        List<Match> nextRoundMatches = createSwissSystemMatches(players, tournamentId, currentRound);
        matchRepository.saveAll(nextRoundMatches);
    }

    private int determineCurrentRound(Long tournamentId) {
        return matchRepository.findMaxRoundNumberByTournamentId(tournamentId).orElse(0);
    }

    private boolean isTournamentOver(TournamentDTO tournament) {
        return tournament.getCurrentRound() >= tournament.getMaxRounds();
    }

    @Transactional
    public void finalizeTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);

        for (Match match : matches) {
            if (match.getStatus() != Match.MatchStatus.COMPLETED) {
                match.setStatus(Match.MatchStatus.COMPLETED);
                matchRepository.save(match);
            }
        }
        calculateFinalStandings(tournamentId);
        updatePlayerProfiles(tournamentId);
    }

    @Transactional
    public ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() == Match.MatchStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Match is already completed.");
        }

        Set<MatchPlayer> participants = match.getParticipants();

        MatchPlayer winner = participants.stream()
                .filter(mp -> mp.getPlayerId().equals(winnerPlayerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Winner not found in match participants"));

        MatchPlayer loser = participants.stream()
                .filter(mp -> mp.getPlayerId().equals(loserPlayerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loser not found in match participants"));

        if (isDraw) {
            winner.setResult(MatchPlayer.Result.DRAW);
            loser.setResult(MatchPlayer.Result.DRAW);

            winner.setPoints(winner.getPoints() + 0.5);
            loser.setPoints(loser.getPoints() + 0.5);
        } else {
            winner.setResult(MatchPlayer.Result.WIN);
            loser.setResult(MatchPlayer.Result.LOSS);

            winner.setPoints(winner.getPoints() + 1);
        }

        match.setStatus(Match.MatchStatus.COMPLETED);

        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);

        return ResponseEntity.ok("Match completed successfully.");
    }

    private void calculateFinalStandings(Long tournamentId) {
        // Implement logic to calculate and store final standings
        // This might involve summing up points, breaking ties, etc.
        // Example:
        // List<PlayerStanding> standings = calculateStandings(tournamentId);
        // standingsRepository.saveAll(standings);
    }

    private void updatePlayerProfiles(Long tournamentId) {
        // Implement logic to update player profiles
        // For example, you might update each player's rating or add a tournament to their history
    }

    public TournamentDTO getTournamentDetails(Long tournamentId) {
        return webClientBuilder.build()
                .get()
                .uri(tournamentServiceUrl + "/api/v1/tournaments/public/" + tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }
}