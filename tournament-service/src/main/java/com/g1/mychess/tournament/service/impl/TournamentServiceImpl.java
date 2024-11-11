package com.g1.mychess.tournament.service.impl;

import com.g1.mychess.tournament.client.MatchServiceClient;
import com.g1.mychess.tournament.client.PlayerServiceClient;
import com.g1.mychess.tournament.dto.MatchmakingDTO;
import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.mapper.TournamentMapper;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.AuthenticationService;
import com.g1.mychess.tournament.service.TournamentService;
import com.g1.mychess.tournament.validation.PlayerEligibilityChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    public TournamentServiceImpl(
            TournamentRepository tournamentRepository,
            TournamentPlayerRepository tournamentPlayerRepository,
            AuthenticationService authenticationService,
            PlayerServiceClient playerServiceClient,
            MatchServiceClient matchServiceClient
    ) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.authenticationService = authenticationService;
        this.playerServiceClient = playerServiceClient;
        this.matchServiceClient = matchServiceClient;
    }

    @Override
    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }
        Long userId = authenticationService.getUserIdFromRequest(request);

        Tournament tournament = new Tournament();
        tournament.setAdminId(userId);
        tournament.setName(tournamentDTO.getName());
        tournament.setDescription(tournamentDTO.getDescription());
        tournament.setMaxPlayers(tournamentDTO.getMaxPlayers());
        tournament.setStartDateTime(tournamentDTO.getStartDateTime());
        tournament.setEndDateTime(tournamentDTO.getEndDateTime());
        tournament.setRegistrationStartDate(tournamentDTO.getRegistrationStartDate());
        tournament.setRegistrationEndDate(tournamentDTO.getRegistrationEndDate());
        tournament.setFormat(Tournament.TournamentFormat.valueOf(tournamentDTO.getFormat()));
        tournament.setStatus(Tournament.TournamentStatus.valueOf(tournamentDTO.getStatus()));
        tournament.setMinRating(tournamentDTO.getMinRating());
        tournament.setMaxRating(tournamentDTO.getMaxRating());
        tournament.setAffectsRating(tournamentDTO.isAffectsRating());
        tournament.setMinAge(tournamentDTO.getMinAge());
        tournament.setMaxAge(tournamentDTO.getMaxAge());
        tournament.setRequiredGender(tournamentDTO.getRequiredGender());
        tournament.setCountry(tournamentDTO.getCountry());
        tournament.setRegion(tournamentDTO.getRegion());
        tournament.setCity(tournamentDTO.getCity());
        tournament.setAddress(tournamentDTO.getAddress());
        tournament.setMaxRounds(tournamentDTO.getMaxRounds());
        tournament.setParticipants(new HashSet<>());
        tournament.setTimeControlSetting(tournamentDTO.getTimeControlSetting());
        tournament.setCurrentRound(0);

        Tournament savedTournament = tournamentRepository.save(tournament);
        TournamentDTO savedTournamentDTO = TournamentMapper.toDTO(savedTournament);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentDTO);
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with name: " + name));
        return ResponseEntity.status(HttpStatus.OK).body(TournamentMapper.toDTO(tournament));
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentById(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with tournament id: " + tournamentId));
        return ResponseEntity.status(HttpStatus.OK).body(TournamentMapper.toDTO(tournament));
    }

    @Override
    public ResponseEntity<Page<TournamentDTO>> getAllTournaments(Pageable pageable) {
        Page<Tournament> tournaments = tournamentRepository.findAll(pageable);
        Page<TournamentDTO> tournamentDTOs = tournaments.map(this::convertToDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tournamentDTOs);
    }

    private TournamentDTO convertToDTO(Tournament tournament) {
        return TournamentMapper.toDTO(tournament);
    }

    @Override
    public ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentDTO.getId()));

        Long adminId = authenticationService.getUserIdFromRequest(request);
        if (!tournament.getAdminId().equals(adminId)) {
            throw new UnauthorizedActionException("Only the tournament admin can update the tournament.");
        }

        tournament.setName(tournamentDTO.getName());
        tournament.setDescription(tournamentDTO.getDescription());
        tournament.setMaxPlayers(tournamentDTO.getMaxPlayers());
        tournament.setStartDateTime(tournamentDTO.getStartDateTime());
        tournament.setEndDateTime(tournamentDTO.getEndDateTime());
        tournament.setRegistrationStartDate(tournamentDTO.getRegistrationStartDate());
        tournament.setRegistrationEndDate(tournamentDTO.getRegistrationEndDate());
        tournament.setFormat(Tournament.TournamentFormat.valueOf(tournamentDTO.getFormat()));
        tournament.setStatus(Tournament.TournamentStatus.valueOf(tournamentDTO.getStatus()));
        tournament.setMinRating(tournamentDTO.getMinRating());
        tournament.setMaxRating(tournamentDTO.getMaxRating());
        tournament.setAffectsRating(tournamentDTO.isAffectsRating());
        tournament.setMinAge(tournamentDTO.getMinAge());
        tournament.setMaxAge(tournamentDTO.getMaxAge());
        tournament.setRequiredGender(tournamentDTO.getRequiredGender());
        tournament.setCountry(tournamentDTO.getCountry());
        tournament.setRegion(tournamentDTO.getRegion());
        tournament.setCity(tournamentDTO.getCity());
        tournament.setAddress(tournamentDTO.getAddress());
        tournament.setMaxRounds(tournamentDTO.getMaxRounds());
        tournament.setTimeControlSetting(tournamentDTO.getTimeControlSetting());

        Tournament updatedTournament = tournamentRepository.save(tournament);
        TournamentDTO updatedTournamentDTO = TournamentMapper.toDTO(updatedTournament);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTournamentDTO);
    }

    @Override
    public ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        if (tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)) {
            throw new PlayerAlreadySignedUpException("Player is already signed up for this tournament.");
        }

        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(playerId);

        PlayerEligibilityChecker.checkPlayerEligibility(playerDTO, tournament);

        TournamentPlayer tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setTournament(tournament);
        tournamentPlayer.setPlayerId(playerId);
        tournamentPlayer.setSignUpDateTime(LocalDateTime.now());
        tournamentPlayer.setStatus(TournamentPlayer.TournamentPlayerStatus.ACTIVE);

        tournament.getParticipants().add(tournamentPlayer);
        tournamentRepository.save(tournament);

        return ResponseEntity.status(HttpStatus.OK).body("Player successfully signed up to the tournament");
    }

    @Override
    @Transactional
    public ResponseEntity<String> startTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);

        verifyAdmin(tournament.getAdminId(), request);

        initializeTournament(tournament);

        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);

        String jwtToken = authenticationService.extractJwtToken(request);
        matchServiceClient.runMatchmaking(matchmakingDTO, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body("Tournament started successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> removePlayerFromTournament(Long tournamentId, Long playerId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);

        verifyAdmin(tournament.getAdminId(), request);

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        TournamentPlayer tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournamentId, playerId)
                .orElseThrow(() -> new PlayerNotSignedUpException("Player is not signed up for this tournament."));

        tournamentPlayerRepository.delete(tournamentPlayer);

        return ResponseEntity.status(HttpStatus.OK).body("Player removed from tournament successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> leaveTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        Long playerId = authenticationService.getUserIdFromRequest(request);

        TournamentPlayer tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournamentId, playerId)
                .orElseThrow(() -> new PlayerNotSignedUpException("Player is not signed up for this tournament."));

        tournamentPlayerRepository.delete(tournamentPlayer);

        return ResponseEntity.status(HttpStatus.OK).body("You have left the tournament successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);

        verifyAdmin(tournament.getAdminId(), request);

        if (tournament.getCurrentRound() >= tournament.getMaxRounds()) {
            throw new IllegalStateException("Cannot start the next round. The tournament has reached the maximum number of rounds. Please finalize the tournament.");
        }

        // Increment the round number
        tournament.setCurrentRound(tournament.getCurrentRound() + 1);
        tournamentRepository.save(tournament);

        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);

        String jwtToken = authenticationService.extractJwtToken(request);
        matchServiceClient.prepareNextRound(matchmakingDTO, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body("Next round prepared successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> completeTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = getTournamentById(tournamentId);

        verifyAdmin(tournament.getAdminId(), request);

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        MatchmakingDTO matchmakingDTO = createMatchmakingDTO(tournament);

        String jwtToken = authenticationService.extractJwtToken(request);
        matchServiceClient.finalizeTournament(matchmakingDTO, jwtToken);
        markTournamentAsCompleted(tournamentId);

        return ResponseEntity.status(HttpStatus.OK).body("Tournament completed successfully.");
    }

    @Override
    public ResponseEntity<List<PlayerDTO>> getPlayersByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

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

    private Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));
    }

    private void verifyAdmin(Long tournamentAdminId, HttpServletRequest request) {
        Long adminId = authenticationService.getUserIdFromRequest(request);
        if (!tournamentAdminId.equals(adminId)) {
            throw new UnauthorizedActionException("Only the tournament admin can perform this action.");
        }
    }

    private void initializeTournament(Tournament tournament) {
        tournament.setCurrentRound(1);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);
    }

    private MatchmakingDTO createMatchmakingDTO(Tournament tournament) {
        Set<TournamentPlayerDTO> participants = tournament.getParticipants().stream()
                .map(TournamentMapper::toTournamentPlayerDTO)
                .collect(Collectors.toSet());

        return new MatchmakingDTO(
                tournament.getId(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                participants
        );
    }

    private void markTournamentAsCompleted(Long tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);
        tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }

   
}