package com.g1.mychess.tournament.service.impl;

import com.g1.mychess.tournament.dto.MatchmakingDTO;
import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.TournamentService;
import com.g1.mychess.tournament.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerRepository tournamentPlayerRepository;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${match.service.url}")
    private String matchServiceUrl;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, TournamentPlayerRepository tournamentPlayerRepository, JwtUtil jwtUtil, WebClient.Builder webClientBuilder) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.jwtUtil = jwtUtil;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }
        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);

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
        tournament.setTimeControlSetting(tournamentDTO.getTimeControl());
        tournament.setCurrentRound(0);

        Tournament savedTournament = tournamentRepository.save(tournament);
        TournamentDTO savedTournamentDTO = convertToDTO(savedTournament);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentDTO);
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with name: " + name));
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(tournament));
    }

    @Override
    public ResponseEntity<TournamentDTO> findTournamentById(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with tournament id: " + tournamentId));
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(tournament));
    }

    @Override
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        List<TournamentDTO> tournamentDTOs = tournaments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(tournamentDTOs);
    }

    @Override
    public ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentDTO.getId()));

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long adminId = jwtUtil.extractUserId(jwtToken);
        if (tournament.getAdminId() != adminId) {
            throw new UnauthorizedActionException("Only the tournament admin can update the tournament.");
        }

        tournament.setAdminId(adminId);
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

        Tournament updatedTournament = tournamentRepository.save(tournament);
        TournamentDTO updatedTournamentDTO = convertToDTO(updatedTournament);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTournamentDTO);
    }

    @Override
    public ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        boolean isAlreadySignedUp = tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId);
        if (isAlreadySignedUp) {
            throw new PlayerAlreadySignedUpException("Player is already signed up for this tournament.");
        }

        PlayerDTO playerDTO;
        try {
            playerDTO = getPlayerDetails(playerId);
        } catch (PlayerNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }

        if (playerDTO.isBlacklisted()) {
            throw new PlayerBlacklistedException("Player is blacklisted from participating in tournaments.");
        }

        if (playerDTO.getGlickoRating() < tournament.getMinRating() || playerDTO.getGlickoRating() > tournament.getMaxRating()) {
            throw new RequirementNotMetException("Player does not meet the rating requirements");
        }

        if (tournament.getMinAge() != null && playerDTO.getAge() < tournament.getMinAge()) {
            throw new RequirementNotMetException("Player does not meet the minimum age requirement");
        }
        if (tournament.getMaxAge() != null && playerDTO.getAge() > tournament.getMaxAge()) {
            throw new RequirementNotMetException("Player does not meet the maximum age requirement");
        }

        if (tournament.getRequiredGender() != null && !tournament.getRequiredGender().equals(playerDTO.getGender()) && !tournament.getRequiredGender().equals("ANY")) {
            throw new RequirementNotMetException("Player does not meet the gender requirement");
        }

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
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        // Verify that the request comes from the admin
        String jwtToken = request.getHeader("Authorization").substring(7);
        Long adminId = jwtUtil.extractUserId(jwtToken);
        if (tournament.getAdminId() != adminId) {
            throw new UnauthorizedActionException("Only the tournament admin can start the tournament.");
        }

        // Set the first round and update the tournament status
        tournament.setCurrentRound(1);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);

        Set<TournamentPlayerDTO> tournamentPlayerDTOs = tournament.getParticipants().stream()
                .map(this::convertToTournamentPlayerDTO)  // Convert each player to TournamentPlayerDTO
                .collect(Collectors.toSet());

        // Prepare the MatchmakingRequestDTO with necessary tournament details
        MatchmakingDTO matchmakingDTO = new MatchmakingDTO(
                tournament.getId(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                tournamentPlayerDTOs
        );

        // Send the matchmaking request to the match service
        runMatchmaking(matchmakingDTO, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body("Tournament started successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> removePlayerFromTournament(Long tournamentId, Long playerId, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long adminId = jwtUtil.extractUserId(jwtToken);

        if (!tournament.getAdminId().equals(adminId)) {
            throw new UnauthorizedActionException("Only the tournament admin can remove a player.");
        }

        TournamentPlayer tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournamentId, playerId)
                .orElseThrow(() -> new PlayerNotSignedUpException("Player is not signed up for this tournament."));

        tournamentPlayerRepository.delete(tournamentPlayer);

        return ResponseEntity.status(HttpStatus.OK).body("Player removed from tournament successfully.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> leaveTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long playerId = jwtUtil.extractUserId(jwtToken);

        TournamentPlayer tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournamentId, playerId)
                .orElseThrow(() -> new PlayerNotSignedUpException("Player is not signed up for this tournament."));

        tournamentPlayerRepository.delete(tournamentPlayer);

        return ResponseEntity.status(HttpStatus.OK).body("You have left the tournament successfully.");
    }

    private void runMatchmaking(MatchmakingDTO matchmakingRequestDTO, String jwtToken) {
        webClientBuilder.build()
                .post()
                .uri(matchServiceUrl + "/api/v1/matches/admin/matchmaking/" + matchmakingRequestDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingRequestDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    @Transactional
    public ResponseEntity<String> prepareNextRound(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        if (tournament.getCurrentRound() >= tournament.getMaxRounds()) {
            throw new IllegalStateException("Cannot start the next round. The tournament has reached the maximum number of rounds. Please finalize the tournament.");
        }

        // Increment the round number
        tournament.setCurrentRound(tournament.getCurrentRound() + 1);
        tournamentRepository.save(tournament);

        // Convert participants to TournamentPlayerDTO
        Set<TournamentPlayerDTO> tournamentPlayerDTOs = tournament.getParticipants().stream()
                .map(this::convertToTournamentPlayerDTO)  // Convert each player to TournamentPlayerDTO
                .collect(Collectors.toSet());

        // Prepare the MatchmakingDTO with updated round and participants
        MatchmakingDTO matchmakingDTO = new MatchmakingDTO(
                tournament.getId(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                tournamentPlayerDTOs
        );

        String jwtToken = request.getHeader("Authorization").substring(7);
        runPrepareNextRoundInMatchService(matchmakingDTO, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body("Next round prepared successfully.");
    }

    private void runPrepareNextRoundInMatchService(MatchmakingDTO matchmakingDTO, String jwtToken) {
        webClientBuilder.build()
                .post()
                .uri(matchServiceUrl + "/api/v1/matches/admin/prepare-next-round/" + matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    @Transactional
    public ResponseEntity<String> completeTournament(Long tournamentId, HttpServletRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Tournament already completed.");
        }

        // Prepare MatchmakingDTO
        Set<TournamentPlayerDTO> participants = tournament.getParticipants().stream()
                .map(this::convertToTournamentPlayerDTO)
                .collect(Collectors.toSet());

        MatchmakingDTO matchmakingDTO = new MatchmakingDTO(
                tournament.getId(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                participants
        );

        // Finalize the tournament in match service
        String jwtToken = request.getHeader("Authorization").substring(7);
        finalizeTournamentInMatchService(matchmakingDTO, jwtToken);
        markTournamentAsCompleted(tournamentId);

        return ResponseEntity.status(HttpStatus.OK).body("Tournament completed successfully.");
    }

    private void finalizeTournamentInMatchService(MatchmakingDTO matchmakingDTO, String jwtToken) {
        webClientBuilder.build()
                .post()
                .uri(matchServiceUrl + "/api/v1/matches/admin/finalize/" + matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void markTournamentAsCompleted(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with tournament id: " + tournamentId));
        tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }

    private TournamentDTO convertToDTO(Tournament tournament) {
        Set<TournamentPlayerDTO> participantDTOs = tournament.getParticipants().stream()
                .map(player -> new TournamentPlayerDTO(
                        player.getTournament().getId(),
                        player.getPlayerId(),
                        player.getSignUpDateTime(),
                        player.getGlickoRating(),
                        player.getRatingDeviation(),
                        player.getVolatility(),
                        player.getPoints(),
                        player.getRoundsPlayed(),
                        player.getStatus().name()
                ))
                .collect(Collectors.toSet());

        return new TournamentDTO(
                tournament.getId(),
                tournament.getAdminId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getMaxPlayers(),
                tournament.getStartDateTime(),
                tournament.getEndDateTime(),
                tournament.getRegistrationStartDate(),
                tournament.getRegistrationEndDate(),
                tournament.getFormat().name(),
                tournament.getStatus().name(),
                tournament.getMinRating(),
                tournament.getMaxRating(),
                tournament.isAffectsRating(),
                tournament.getMinAge(),
                tournament.getMaxAge(),
                tournament.getRequiredGender(),
                tournament.getCountry(),
                tournament.getRegion(),
                tournament.getCity(),
                tournament.getAddress(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                participantDTOs,
                tournament.getTimeControlSetting()
        );
    }

    private TournamentPlayerDTO convertToTournamentPlayerDTO(TournamentPlayer tournamentPlayer) {
        return new TournamentPlayerDTO(
                tournamentPlayer.getTournament().getId(),
                tournamentPlayer.getPlayerId(),
                tournamentPlayer.getSignUpDateTime(),
                tournamentPlayer.getGlickoRating(),
                tournamentPlayer.getRatingDeviation(),
                tournamentPlayer.getVolatility(),
                tournamentPlayer.getPoints(),
                tournamentPlayer.getRoundsPlayed(),
                tournamentPlayer.getStatus().name()
        );
    }

    private PlayerDTO getPlayerDetails(Long playerId) {
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/" + playerId + "/details")
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();
    }
}