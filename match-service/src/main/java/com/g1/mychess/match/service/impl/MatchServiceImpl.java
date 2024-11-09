package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.*;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.service.Glicko2RatingService;
import com.g1.mychess.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final WebClient.Builder webClientBuilder;
    private final Glicko2RatingService glicko2RatingService;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, WebClient.Builder webClientBuilder, Glicko2RatingService glicko2RatingService) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.webClientBuilder = webClientBuilder;
        this.glicko2RatingService = glicko2RatingService;
    }

    @Override
    @Transactional
    public void runMatchmaking(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int currentRound = matchmakingDTO.getCurrentRound();
        Set<TournamentPlayerDTO> participants = matchmakingDTO.getParticipants();

        List<MatchPlayer> players = initializePlayers(participants, currentRound);

        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(newMatches);
        saveMatchPlayers(newMatches);
    }

    private List<Match> createSwissSystemMatches(List<MatchPlayer> players, Long tournamentId, int currentRound) {
        List<Match> matches = new ArrayList<>();
        players = sortPlayersByPointsAndRating(players);

        Set<Long> pairedPlayers = new HashSet<>();
        for (int i = 0; i < players.size() - 1; i += 2) {
            Match match = createMatch(players.get(i), players.get(i + 1), tournamentId, currentRound, pairedPlayers);
            if (match != null) {
                matches.add(match);
            }
        }

        handleByeIfNeeded(players, tournamentId, currentRound);
        return matches;
    }

    private List<MatchPlayer> initializePlayers(Set<TournamentPlayerDTO> participants, int currentRound) {
        List<MatchPlayer> players = new ArrayList<>();

        for (TournamentPlayerDTO participant : participants) {
            MatchPlayer player = new MatchPlayer();
            player.setPlayerId(participant.getPlayerId());
            player.setGlickoRating(participant.getGlickoRating());
            player.setRatingDeviation(participant.getRatingDeviation());
            player.setVolatility(participant.getVolatility());
            player.setCurrentRound(currentRound);

            // If first round, set points to 0, otherwise carry forward points
            if (currentRound == 1) {
                player.setPoints(0);
            } else {
                MatchPlayer previousRoundPlayer = matchPlayerRepository.findByPlayerIdAndMatch_TournamentIdAndCurrentRound(participant.getPlayerId(), participant.getTournamentId(), currentRound - 1);
                player.setPoints(previousRoundPlayer.getPoints());
            }

            players.add(player);
        }

        return players;
    }

    private void saveMatchPlayers(List<Match> matches) {
        List<MatchPlayer> matchPlayers = new ArrayList<>();
        for (Match match : matches) {
            matchPlayers.addAll(match.getParticipants());  // Get the participants from the match
        }
        matchPlayerRepository.saveAll(matchPlayers);  // Save match players to the repository after matches are created
    }

    private Match createMatch(MatchPlayer player1, MatchPlayer player2, Long tournamentId, int currentRound, Set<Long> pairedPlayers) {
        if (pairedPlayers.contains(player1.getPlayerId()) || pairedPlayers.contains(player2.getPlayerId())) return null;

        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setScheduledTime(LocalDateTime.now().plusDays(1));
        match.setStatus(Match.MatchStatus.SCHEDULED);
        match.setRoundNumber(currentRound);  // Use the correct round number

        // Set the match players as participants
        match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
        player1.setMatch(match);
        player2.setMatch(match);

        player1.setOpponentId(player2.getPlayerId());
        player2.setOpponentId(player1.getPlayerId());

        pairedPlayers.add(player1.getPlayerId());
        pairedPlayers.add(player2.getPlayerId());

        return match;
    }

    private void handleByeIfNeeded(List<MatchPlayer> players, Long tournamentId, int currentRound) {
        if (players.size() % 2 != 0) {
            MatchPlayer byePlayer = players.get(players.size() - 1);
            byePlayer.setPoints(byePlayer.getPoints() + 1);

            Match byeMatch = new Match();
            byeMatch.setTournamentId(tournamentId);
            byeMatch.setScheduledTime(LocalDateTime.now().plusDays(1));
            byeMatch.setStatus(Match.MatchStatus.COMPLETED);
            byeMatch.setRoundNumber(currentRound);

            byeMatch.setParticipants(Collections.singleton(byePlayer));

            matchRepository.save(byeMatch);
            matchPlayerRepository.save(byePlayer);
        }
    }

    private List<MatchPlayer> sortPlayersByPointsAndRating(List<MatchPlayer> players) {
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getGlickoRating)
                .reversed());
        return players;
    }

    @Override
    @Transactional
    public void prepareNextRound(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int currentRound = matchmakingDTO.getCurrentRound();
        Set<TournamentPlayerDTO> participants = matchmakingDTO.getParticipants();

        List<MatchPlayer> players = initializePlayers(participants, currentRound);
        startNextRound(tournamentId, players, currentRound);
    }

    private void startNextRound(Long tournamentId, List<MatchPlayer> players, int currentRound) {
        List<Match> nextRoundMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(nextRoundMatches);
    }

    @Override
    @Transactional
    public ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() == Match.MatchStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Match is already completed.");
        }

        Set<MatchPlayer> participants = match.getParticipants();

        if (isDraw) {
            participants.forEach(p -> {
                p.setResult(MatchPlayer.Result.DRAW);
                p.setPoints(p.getPoints() + 0.5);
            });
        } else {
            if (winnerPlayerId == null || loserPlayerId == null) {
                throw new IllegalArgumentException("Winner and loser must be provided if not draw");
            }

            MatchPlayer winner = participants.stream()
                    .filter(mp -> mp.getPlayerId().equals(winnerPlayerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Winner not found in match participants"));

            MatchPlayer loser = participants.stream()
                    .filter(mp -> mp.getPlayerId().equals(loserPlayerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Loser not found in match participants"));

            winner.setResult(MatchPlayer.Result.WIN);
            winner.setPoints(winner.getPoints() + 1);
            loser.setResult(MatchPlayer.Result.LOSS);
        }

        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);

        return ResponseEntity.ok("Match completed successfully.");
    }

    @Override
    @Transactional
    public void finalizeTournament(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int maxRounds = matchmakingDTO.getMaxRounds();

        if (matchmakingDTO.getCurrentRound() < maxRounds) {
            throw new IllegalStateException("Cannot complete tournament. Rounds not completed yet.");
        }

        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        for (Match match : matches) {
            if (match.getStatus() == Match.MatchStatus.SCHEDULED) {
                throw new IllegalStateException("Cannot complete tournament. Matches not completed yet.");
            }
        }

        List<MatchPlayer> matchPlayers = matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournamentId, matchmakingDTO.getMaxRounds()); // what if matchPlayer is bye on last round

        for (MatchPlayer player : matchPlayers) {
            List<MatchPlayer> matchPlayerList = matchPlayerRepository.findByPlayerIdAndMatch_TournamentId(player.getPlayerId(), tournamentId);
            List<MatchPlayer> opponents = getAllOpponents(matchPlayerList);
            double[] results = getAllResults(matchPlayerList);

            PlayerRatingUpdateDTO playerRatingUpdate = glicko2RatingService.calculatePlayerRatings(player, opponents, results);

            updatePlayerProfileRating(playerRatingUpdate);
        }
    }

    public List<MatchPlayer> getAllOpponents(List<MatchPlayer> matchPlayerList) {
        List<MatchPlayer> opponents = new ArrayList<>();

        for (MatchPlayer matchPlayer : matchPlayerList) {
            opponents.add(matchPlayerRepository.findByPlayerIdAndMatchId(matchPlayer.getOpponentId(), matchPlayer.getMatch().getId()));
        }

        return opponents;
    }

    public double[] getAllResults(List<MatchPlayer> matchPlayerList) {
        double[] result = new double[matchPlayerList.size()];
        for (int i = 0; i < matchPlayerList.size(); i++) {
            if (matchPlayerList.get(i).getResult() == MatchPlayer.Result.WIN) {
                result[i] = 1.0;
            } else if (matchPlayerList.get(i).getResult() == MatchPlayer.Result.DRAW) {
                result[i] = 0.5;
            } else {
                result[i] = 0.0;
            }
        }
        return result;
    }

    private void updatePlayerProfileRating(PlayerRatingUpdateDTO ratingUpdate) {
        webClientBuilder.build()
                .post()
                .uri(playerServiceUrl + "/api/v1/profile/update-rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        return convertToDTOList(matches);
    }

    // Convert a list of Match objects to a list of MatchDTOs
    public List<MatchDTO> convertToDTOList(List<Match> matches) {
        return matches.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        List<Match> matches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));

        return convertToDTOList(matches);
    }

    private MatchDTO convertToDTO(Match match) {
        return MatchDTO.fromEntity(match);
    }
}