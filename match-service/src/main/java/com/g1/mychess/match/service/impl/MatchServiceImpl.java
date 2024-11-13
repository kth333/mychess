package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.client.PlayerServiceClient;
import com.g1.mychess.match.client.PlayerServiceClient;
import com.g1.mychess.match.client.TournamentServiceClient;
import com.g1.mychess.match.client.EmailServiceClient;
import com.g1.mychess.match.client.EmailServiceClient;
import com.g1.mychess.match.dto.*;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.mapper.MatchMapper;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.MatchService;
import com.g1.mychess.match.service.MatchTimeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service implementation for match-related operations in the chess application.
 * Provides methods to retrieve matches by tournament, round, and match results.
 */
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final EmailServiceClient emailServiceClient;
    private final PlayerServiceClient playerServiceClient;
    private final MatchTimeServiceImpl matchTimeService;

    /**
     * Constructor to initialize repositories.
     *
     * @param matchRepository          The repository for match data.
     * @param matchPlayerRepository    The repository for match player data.
     */
    @Autowired
    public MatchServiceImpl(
            MatchRepository matchRepository,
            MatchPlayerRepository matchPlayerRepository,
            EmailServiceClient emailServiceClient,
            PlayerServiceClient playerServiceClient,
            MatchTimeServiceImpl matchTimeService
    ) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.emailServiceClient = emailServiceClient;
        this.playerServiceClient = playerServiceClient;
        this.matchTimeService = matchTimeService;
    }

    /**
     * Retrieves all matches associated with a given tournament.
     *
     * @param tournamentId The ID of the tournament.
     * @return A list of MatchDTO objects representing the matches in the tournament.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     */
    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        return MatchMapper.toDTOList(matches);
    }

    /**
     * Retrieves all matches associated with a specific round of a given tournament.
     * It first checks whether the tournament exists and then fetches matches for the specified round.
     *
     * @param tournamentId The ID of the tournament.
     * @param roundNumber  The round number of the tournament.
     * @return A list of MatchDTO objects representing the matches in the specified round.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     * @throws TournamentRoundNotFoundException if the round number does not exist for the tournament.
     */
    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        List<Match> matches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));

        return MatchMapper.toDTOList(matches);
    }

    /**
     * Retrieves all match results associated with a given tournament.
     * Builds a MatchResultDTO for each match containing information about the winner, loser, or draw.
     *
     * @param tournamentId The ID of the tournament.
     * @return A list of MatchResultDTO objects representing the results of matches in the tournament.
     * @throws TournamentNotFoundException if the tournament with the given ID does not exist.
     */
    @Override
    @Transactional
    public List<MatchResultDTO> findAllMatchResultsByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        List<MatchResultDTO> matchResults = new ArrayList<>();

        for (Match match : matches) {
            List<MatchPlayer> participants = matchPlayerRepository.findByMatchId(match.getId());
            if (participants.size() != 2) {
                continue; // Skip incomplete match data
            }

            MatchResultDTO matchResult = buildMatchResult(participants);
            if (matchResult != null) {
                matchResults.add(matchResult);
            }
        }

        return matchResults;
    }

    /**
     * Builds a MatchResultDTO object based on the match participants' results.
     * It processes the results, determining whether the match was a draw, win, or loss.
     *
     * @param participants A list of MatchPlayer objects representing the participants in the match.
     * @return A MatchResultDTO representing the result of the match, or null if the result is incomplete.
     */
    private MatchResultDTO buildMatchResult(List<MatchPlayer> participants) {
        MatchResultDTO matchResult = new MatchResultDTO();
        boolean isDraw = false;
        Long winnerId = null;
        Long loserId = null;

        for (MatchPlayer participant : participants) {
            if (participant.getResult() == null) {
                continue; // Skip if the result is not set yet
            }

            switch (participant.getResult()) {
                case DRAW:
                    isDraw = true;
                    break;
                case WIN:
                    winnerId = participant.getPlayerId();
                    break;
                case LOSS:
                    loserId = participant.getPlayerId();
                    break;
            }
        }

        if (isDraw) {
            matchResult.setIsDraw(true);
        } else if (winnerId != null) {
            matchResult.setWinnerId(winnerId);
            if (loserId != null) {
                matchResult.setLoserId(loserId);
            }
        }

        return matchResult.getMatchId() != null ? matchResult : null; // Only return non-null results
    }

    /**
     * Retrieves the results of a specific tournament based on the provided tournament ID.
     * This method queries the match players in the tournament, orders them by points in descending
     * order, and then maps the results to a {@link TournamentResultsDTO}.
     *
     * @param tournamentId The ID of the tournament for which the results are to be fetched.
     * @return A {@link TournamentResultsDTO} containing the ordered results of the specified tournament.
     */
    @Override
    @Transactional
    public TournamentResultsDTO getTournamentResults(Long tournamentId) {
        List<MatchPlayer> matchPlayers = matchPlayerRepository.findByMatch_TournamentIdOrderByPointsDesc(tournamentId);
        return MatchMapper.toTournamentResultsDTO(matchPlayers);
    }

    /**
     * Sends reminders for upcoming matches scheduled within the next hour.
     * This method runs every hour (configured with @Scheduled annotation), and retrieves matches
     * that are scheduled to start in the next hour, then sends reminder emails to the participants.
     */
    @Override
    @Scheduled(fixedRate = 3600000) // Runs every hour
    @Transactional
    public void sendMatchReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        List<Match> upcomingMatches = matchRepository.findByScheduledTimeBetween(now, oneHourLater);

        for (Match match : upcomingMatches) {
            sendReminderEmails(match);
        }
    }

    /**
     * Sends reminder emails to the participants of a specific match.
     * This method retrieves the email addresses of all match participants and sends them a reminder
     * about the upcoming match using {@link #sendReminderEmail(String, Match)}.
     *
     * @param match The match object containing the participants and scheduled time.
     */
    private void sendReminderEmails(Match match) {
        Set<MatchPlayer> participants = match.getParticipants();

        for (MatchPlayer participant : participants) {
            String email = playerServiceClient.getPlayerDetails(participant.getPlayerId()).getEmail();
            sendReminderEmail(email, match);
        }
    }

    /**
     * Sends a reminder email to a specific player for a particular match.
     * This method constructs a {@link ReminderEmailDTO} object and uses the {@link EmailServiceClient}
     * to send the email reminder to the player.
     *
     * @param email The recipient player's email address.
     * @param match The match details (tournament name and scheduled time) to include in the reminder.
     */
    private void sendReminderEmail(String email, Match match) {
        ReminderEmailDTO emailDTO = new ReminderEmailDTO();
        emailDTO.setTo(email);
        emailDTO.setTournamentName(matchTimeService.getTournamentById(match.getTournamentId()).getName());
        emailDTO.setScheduledTime(match.getScheduledTime());

        emailServiceClient.sendMatchReminderEmail(emailDTO);
    }
}