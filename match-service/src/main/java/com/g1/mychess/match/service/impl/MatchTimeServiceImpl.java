package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.client.TournamentServiceClient;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import com.g1.mychess.match.exception.UnauthorizedActionException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.AuthenticationService;
import com.g1.mychess.match.service.MatchResultService;
import com.g1.mychess.match.service.MatchTimeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of the {@link MatchTimeService} interface.
 * Handles operations for updating the match time for ongoing matches.
 * Ensures that only the tournament admin is allowed to perform the update.
 */
@Service
public class MatchTimeServiceImpl implements MatchTimeService {

    private final MatchRepository matchRepository;
    private final TournamentServiceClient tournamentServiceClient;
    private final AuthenticationService authenticationService;
    private final MatchResultServiceImpl matchResultService;

    /**
     * Constructor to initialize necessary dependencies for match time updates.
     *
     * @param matchRepository The repository for accessing and modifying match data.
     * @param tournamentServiceClient The client for fetching tournament details.
     * @param authenticationService Service for handling user authentication.
     * @param matchResultService Service for accessing match results.
     */
    public MatchTimeServiceImpl(MatchRepository matchRepository, TournamentServiceClient tournamentServiceClient, AuthenticationService authenticationService, MatchResultServiceImpl matchResultService) {
        this.matchRepository = matchRepository;
        this.tournamentServiceClient = tournamentServiceClient;
        this.authenticationService = authenticationService;
        this.matchResultService = matchResultService;
    }

    /**
     * Updates the scheduled time of a match. Only the tournament admin is authorized to update the time.
     * <p>
     * The method fetches the match and its associated tournament details, checks if the current user is
     * the admin of the tournament, and if authorized, updates the match's scheduled time.
     * </p>
     *
     * @param matchId The unique identifier of the match to update.
     * @param updateDTO A data transfer object containing the updated scheduled time.
     * @param request The HTTP request object, which contains user authentication details.
     * @return A {@link ResponseEntity} indicating whether the match time update was successful.
     *
     * @throws UnauthorizedActionException If the current user is not the tournament admin.
     */
    @Override
    @Transactional
    public ResponseEntity<String> updateMatchTime(Long matchId, UpdateMatchTimeDTO updateDTO, HttpServletRequest request) {
        Long adminId = extractAdminId(request);

        Match match = matchResultService.getMatch(matchId);
        TournamentDTO tournament = getTournamentById(match.getTournamentId());

        if (!isTournamentAdmin(adminId, tournament)) {
            throw new UnauthorizedActionException("Only the tournament admin can update the match time.");
        }

        updateScheduledTime(match, updateDTO.getScheduledTime());
        return ResponseEntity.ok("Match time updated successfully.");
    }

    /**
     * Retrieves the tournament details using the tournament service client.
     *
     * @param tournamentId The unique identifier of the tournament.
     * @return A {@link TournamentDTO} containing tournament details.
     */
    public TournamentDTO getTournamentById(Long tournamentId) {
        return tournamentServiceClient.getTournamentDetails(tournamentId);
    }

    /**
     * Extracts the admin ID from the HTTP request.
     *
     * @param request The HTTP request containing user details.
     * @return The ID of the user making the request.
     */
    private Long extractAdminId(HttpServletRequest request) {
        return authenticationService.getUserIdFromRequest(request);
    }

    /**
     * Checks if the current user is the admin of the tournament.
     *
     * @param adminId The ID of the user requesting the update.
     * @param tournament The tournament details.
     * @return True if the user is the tournament admin, false otherwise.
     */
    private boolean isTournamentAdmin(Long adminId, TournamentDTO tournament) {
        return tournament.getAdminId().equals(adminId);
    }

    /**
     * Updates the scheduled time of a match.
     *
     * @param match The match to update.
     * @param newScheduledTime The new scheduled time to set for the match.
     */
    private void updateScheduledTime(Match match, LocalDateTime newScheduledTime) {
        match.setScheduledTime(newScheduledTime);
        matchRepository.save(match);
    }
}
