package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.MatchResultDTO;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
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
    private final WebClient.Builder webClientBuilder;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    public MatchService(MatchRepository matchRepository, WebClient.Builder webClientBuilder) {
        this.matchRepository = matchRepository;
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

        List<MatchPlayer> players = getTournamentPlayers(tournament);

        // Implement Swiss-system matchmaking logic here
        List<Match> newMatches = createSwissSystemMatches(players);

        newMatches.forEach(matchRepository::save);
    }

    private List<MatchPlayer> getTournamentPlayers(TournamentDTO tournament) {
        return tournament.getParticipants().stream()
                .map(participant -> {
                    MatchPlayer player = new MatchPlayer();
                    player.setPlayerId(participant.getPlayerId());
                    player.setInitialRating(participant.getGlickoRating()); // Assuming Glicko rating is available in participant DTO
                    return player;
                })
                .collect(Collectors.toList());
    }

    private List<Match> createSwissSystemMatches(List<MatchPlayer> players) {
        List<Match> matches = new ArrayList<>();

        // Sort players by their Glicko rating (descending)
        players.sort(Comparator.comparingInt(MatchPlayer::getInitialRating).reversed());

        // Pair players in a Swiss system
        for (int i = 0; i < players.size() - 1; i += 2) {
            MatchPlayer player1 = players.get(i);
            MatchPlayer player2 = players.get(i + 1);

            Match match = new Match();
            match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
            match.setTournamentId(player1.getMatch().getTournamentId());
            match.setScheduledTime(LocalDateTime.now().plusDays(1)); // Schedule the match for the next day
            match.setStatus(Match.MatchStatus.SCHEDULED);

            matches.add(match);
        }

        // Handle odd number of players by assigning a bye
        if (players.size() % 2 != 0) {
            MatchPlayer byePlayer = players.get(players.size() - 1); // The last player in the sorted list

            assignByeToPlayer(byePlayer);
        }

        return matches;
    }
    private void assignByeToPlayer(MatchPlayer byePlayer) {
        // Award a point to the player for receiving a bye
        byePlayer.setResult(MatchPlayer.Result.WIN);

        // match to record the bye
        Match byeMatch = new Match();
        byeMatch.setParticipants(Collections.singleton(byePlayer));
        byeMatch.setTournamentId(byePlayer.getMatch().getTournamentId());
        byeMatch.setScheduledTime(LocalDateTime.now().plusDays(1));
        byeMatch.setStatus(Match.MatchStatus.COMPLETED);

        matchRepository.save(byeMatch);
    }

    @Transactional
    public void prepareNextRound(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);
        List<MatchPlayer> players = getTournamentPlayers(tournament);

        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints).reversed()
                .thenComparing(MatchPlayer::getInitialRating).reversed());

        if (isTournamentOver(tournament)) {
            finalizeTournament(tournamentId);
            return;
        }

        List<Match> nextRoundMatches = createSwissSystemMatches(players);

        nextRoundMatches.forEach(matchRepository::save);
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
                .uri(tournamentServiceUrl + "/api/v1/tournaments/" + tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }
}