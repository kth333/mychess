package com.g1.mychess.tournament.service.impl;

import com.g1.mychess.tournament.client.EmailServiceClient;
import com.g1.mychess.tournament.client.MatchServiceClient;
import com.g1.mychess.tournament.client.PlayerServiceClient;
import com.g1.mychess.tournament.dto.*;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.mapper.TournamentMapper;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.AuthenticationService;
import com.g1.mychess.tournament.service.TournamentService;
import com.g1.mychess.tournament.util.EmailContentBuilder;
import com.g1.mychess.tournament.validation.PlayerEligibilityChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerRepository tournamentPlayerRepository;
    private final AuthenticationService authenticationService;
    private final PlayerServiceClient playerServiceClient;
    private final MatchServiceClient matchServiceClient;
    private final EmailServiceClient emailServiceClient;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 TournamentPlayerRepository tournamentPlayerRepository,
                                 AuthenticationService authenticationService,
                                 PlayerServiceClient playerServiceClient,
                                 MatchServiceClient matchServiceClient,
                                 EmailServiceClient emailServiceClient) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.authenticationService = authenticationService;
        this.playerServiceClient = playerServiceClient;
        this.matchServiceClient = matchServiceClient;
        this.emailServiceClient = emailServiceClient;
    }

    @Override
    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }

        Long adminId = authenticationService.getUserIdFromRequest(request);
        Tournament tournament = TournamentMapper.toEntity(tournamentDTO);
        tournament.setAdminId(adminId);
        tournament.setParticipants(Set.of());
        tournament.setCurrentRound(0);

        Tournament savedTournament = tournamentRepository.save(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(TournamentMapper.toDTO(savedTournament));
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with name: " + name));
        return ResponseEntity.ok(TournamentMapper.toDTO(tournament));
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentById(Long tournamentId) {
        return ResponseEntity.ok(TournamentMapper.toDTO(getTournamentById(tournamentId)));
    }

    @Override
    public ResponseEntity<Page<TournamentDTO>> getAllTournaments(Pageable pageable) {
        Page<Tournament> tournaments = tournamentRepository.findAll(pageable);
        Page<TournamentDTO> tournamentDTOs = tournaments.map(TournamentMapper::toDTO);
        return ResponseEntity.ok(tournamentDTOs);
    }

    @Override
    public ResponseEntity<Page<TournamentDTO>> getUpcomingTournaments(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<Tournament> upcomingTournaments = tournamentRepository.findByStartDateTimeAfterOrderByStartDateTimeAsc(now, pageable);
        Page<TournamentDTO> tournamentDTOs = upcomingTournaments.map(TournamentMapper::toDTO);
        return ResponseEntity.ok(tournamentDTOs);
    }

    @Override
    public ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentDTO.getId());
        verifyAdminAccess(tournament.getAdminId(), request);

        TournamentMapper.updateEntityFromDTO(tournament, tournamentDTO);
        Tournament updatedTournament = tournamentRepository.save(tournament);

        return ResponseEntity.ok(TournamentMapper.toDTO(updatedTournament));
    }

    @Override
    public ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = getTournamentById(tournamentId);

        validateRegistrationPeriod(tournament);
        ensurePlayerNotAlreadySignedUp(tournamentId, playerId);

        PlayerDTO playerDTO = fetchPlayerDetails(playerId);
        validatePlayerEligibilityForTournament(playerDTO, tournament);

        registerPlayerToTournament(tournament, playerDTO);
        sendTournamentSignUpNotification(playerDTO, tournament);

        return ResponseEntity.ok("Player successfully signed up to the tournament");
    }

    @Override
    @Transactional
    public ResponseEntity<String> startTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);
        verifyAdminAccess(tournament.getAdminId(), request);

        initializeTournamentForStart(tournament);

        String jwtToken = extractJwtToken(request);
        runMatchmakingForTournament(tournament, jwtToken);

        return ResponseEntity.ok("Tournament started successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> removePlayerFromTournament(Long tournamentId, Long playerId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);
        verifyAdminAccess(tournament.getAdminId(), request);
        ensureTournamentNotCompleted(tournament);

        removePlayerFromTournament(tournament, playerId);

        return ResponseEntity.ok("Player removed from tournament successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> leaveTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);
        ensureTournamentNotCompleted(tournament);

        Long playerId = authenticationService.getUserIdFromRequest(request);
        removePlayerFromTournament(tournament, playerId);

        return ResponseEntity.ok("You have left the tournament successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);
        verifyAdminAccess(tournament.getAdminId(), request);

        progressTournamentRound(tournament);

        String jwtToken = extractJwtToken(request);
        prepareMatchmakingForNextRound(tournament, jwtToken);

        return ResponseEntity.ok("Next round prepared successfully.");
    }

    @Transactional
    public ResponseEntity<String> completeTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);
        verifyAdminAccess(tournament.getAdminId(), request);
        ensureTournamentNotCompleted(tournament);

        finalizeTournament(tournament, request);

        return ResponseEntity.ok("Tournament completed successfully.");
    }

    @Override
    public ResponseEntity<List<PlayerDTO>> getPlayersByTournament(Long tournamentId) {
        List<Long> playerIds = tournamentPlayerRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("No players found for tournament with id: " + tournamentId))
                .stream()
                .map(TournamentPlayer::getPlayerId)
                .toList();

        List<PlayerDTO> players = playerIds.stream()
                .map(playerServiceClient::getPlayerDetails)
                .collect(Collectors.toList());

        return ResponseEntity.ok(players);
    }

    // Helper Methods
    private void validateRegistrationPeriod(Tournament tournament) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(tournament.getRegistrationStartDate()) || now.isAfter(tournament.getRegistrationEndDate())) {
            throw new RegistrationPeriodException("Registration for this tournament is not open.");
        }
    }

    private void ensurePlayerNotAlreadySignedUp(Long tournamentId, Long playerId) {
        if (tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)) {
            throw new PlayerAlreadySignedUpException("Player is already signed up for this tournament.");
        }
    }

    private PlayerDTO fetchPlayerDetails(Long playerId) {
        return playerServiceClient.getPlayerDetails(playerId);
    }

    private void validatePlayerEligibilityForTournament(PlayerDTO playerDTO, Tournament tournament) {
        PlayerEligibilityChecker.checkPlayerEligibility(playerDTO, tournament);
    }

    private void registerPlayerToTournament(Tournament tournament, PlayerDTO playerDTO) {
        TournamentPlayer tournamentPlayer = createTournamentPlayerInstance(tournament, playerDTO);
        saveTournamentPlayer(tournamentPlayer);
    }

    private TournamentPlayer createTournamentPlayerInstance(Tournament tournament, PlayerDTO playerDTO) {
        TournamentPlayer tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setTournament(tournament);
        tournamentPlayer.setPlayerId(playerDTO.getId());
        tournamentPlayer.setSignUpDateTime(LocalDateTime.now());
        tournamentPlayer.setStatus(TournamentPlayer.TournamentPlayerStatus.ACTIVE);
        tournamentPlayer.setPoints(0.0);
        tournamentPlayer.setGlickoRating(playerDTO.getGlickoRating());
        tournamentPlayer.setRatingDeviation(playerDTO.getRatingDeviation());
        tournamentPlayer.setVolatility(playerDTO.getVolatility());
        return tournamentPlayer;
    }

    private void saveTournamentPlayer(TournamentPlayer tournamentPlayer) {
        tournamentPlayerRepository.save(tournamentPlayer);
    }

    private void sendTournamentSignUpNotification(PlayerDTO playerDTO, Tournament tournament) {
        TournamentNotificationDTO notification = buildTournamentSignUpNotification(playerDTO, tournament);
        emailServiceClient.sendTournamentNotification(notification).subscribe();
    }

    private TournamentNotificationDTO buildTournamentSignUpNotification(PlayerDTO playerDTO, Tournament tournament) {
        TournamentNotificationDTO notification = new TournamentNotificationDTO();
        notification.setTo(playerDTO.getEmail());
        notification.setSubject("Tournament Sign-Up Confirmation");
        notification.setMessage(EmailContentBuilder.buildTournamentSignUpMessage(playerDTO, tournament));
        return notification;
    }

    private void initializeTournamentForStart(Tournament tournament) {
        tournament.setCurrentRound(1);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);
    }

    private String extractJwtToken(HttpServletRequest request) {
        return authenticationService.extractJwtToken(request);
    }

    private void runMatchmakingForTournament(Tournament tournament, String jwtToken) {
        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);
        String tournamentFormat = tournament.getFormat().toString();
        matchServiceClient.runMatchmaking(matchmakingDTO, jwtToken, tournamentFormat);
    }

    private void progressTournamentRound(Tournament tournament) {
        if (tournament.getCurrentRound() >= tournament.getMaxRounds()) {
            throw new IllegalStateException("Cannot start the next round. The tournament has reached the maximum number of rounds.");
        }
        tournament.setCurrentRound(tournament.getCurrentRound() + 1);
        tournamentRepository.save(tournament);
    }

    private void prepareMatchmakingForNextRound(Tournament tournament, String jwtToken) {
        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);
        String tournamentFormat = tournament.getFormat().toString();
        matchServiceClient.runMatchmaking(matchmakingDTO, jwtToken, tournamentFormat);
    }

    private Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));
    }

    private void verifyAdminAccess(Long tournamentAdminId, HttpServletRequest request) {
        Long adminId = authenticationService.getUserIdFromRequest(request);
        if (!tournamentAdminId.equals(adminId)) {
            throw new UnauthorizedActionException("Only the tournament admin can perform this action.");
        }
    }

    private MatchmakingDTO createMatchmakingDTO(Tournament tournament) {
        Set<TournamentPlayerDTO> participants = tournament.getParticipants().stream()
                .map(TournamentMapper::toTournamentPlayerDTO)
                .collect(Collectors.toSet());

        return new MatchmakingDTO(tournament.getId(), tournament.getCurrentRound(), tournament.getMaxRounds(), participants);
    }

    private void finalizeTournament(Tournament tournament, HttpServletRequest request) {
        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);
        String jwtToken = authenticationService.extractJwtToken(request);
        matchServiceClient.finalizeTournament(matchmakingDTO, jwtToken);
        markTournamentAsCompleted(tournament);
    }

    private void ensureTournamentNotCompleted(Tournament tournament) {
        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }
    }

    private void removePlayerFromTournament(Tournament tournament, Long playerId) {
        TournamentPlayer tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournament.getId(), playerId)
                .orElseThrow(() -> new PlayerNotSignedUpException("Player is not signed up for this tournament."));
        tournamentPlayerRepository.delete(tournamentPlayer);
    }

    private void markTournamentAsCompleted(Tournament tournament) {
        tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }
}