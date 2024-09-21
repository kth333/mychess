package com.g1.mychess.tournament.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.util.JwtUtil;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.repository.TournamentRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerRepository tournamentPlayerRepository;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${match.service.url}")
    private String matchServiceUrl;

    public TournamentService(TournamentRepository tournamentRepository, TournamentPlayerRepository tournamentPlayerRepository, JwtUtil jwtUtil, WebClient.Builder webClientBuilder) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.jwtUtil = jwtUtil;
        this.webClientBuilder = webClientBuilder;
    }

    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }
        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);

        // Map TournamentDTO to Tournament entity
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
        tournament.setParticipants(new HashSet<>());
        tournament.setTimeControlSetting(tournamentDTO.getTimeControl());

        // Save the tournament to the repository
        Tournament savedTournament = tournamentRepository.save(tournament);

        // Map the saved Tournament entity back to a TournamentDTO
        TournamentDTO savedTournamentDTO = convertToDTO(savedTournament);

        // Return the saved tournament details
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentDTO);
    }

    public ResponseEntity<TournamentDTO> findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with name: " + name));

        // Convert Tournament to TournamentDTO
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(tournament));
    }

    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        List<TournamentDTO> tournamentDTOs = tournaments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(tournamentDTOs);
    }

    public ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        // Find the tournament by its ID
        Tournament tournament = tournamentRepository.findById(tournamentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentDTO.getId()));

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);

        // Update the tournament fields
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

        // Save the updated tournament to the repository
        Tournament updatedTournament = tournamentRepository.save(tournament);

        // Convert the updated Tournament entity back to a TournamentDTO
        TournamentDTO updatedTournamentDTO = convertToDTO(updatedTournament);

        // Return the updated tournament details
        return ResponseEntity.status(HttpStatus.OK).body(updatedTournamentDTO);
    }

    public ResponseEntity<String> signUpToTournament(Long tournamentId, Long playerId) {
        // Fetch the tournament from the repository
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

        // Check rating requirements
        if (playerDTO.getGlickoRating() < tournament.getMinRating() || playerDTO.getGlickoRating() > tournament.getMaxRating()) {
            throw new RequirementNotMetException("Player does not meet the rating requirements");
        }

        // Check age requirements
        if (tournament.getMinAge() != null && playerDTO.getAge() < tournament.getMinAge()) {
            throw new RequirementNotMetException("Player does not meet the minimum age requirement");
        }
        if (tournament.getMaxAge() != null && playerDTO.getAge() > tournament.getMaxAge()) {
            throw new RequirementNotMetException("Player does not meet the maximum age requirement");
        }

        // Check gender requirements
        if (tournament.getRequiredGender() != null && !tournament.getRequiredGender().equals(playerDTO.getGender()) && !tournament.getRequiredGender().equals("ANY")) {
            throw new RequirementNotMetException("Player does not meet the gender requirement");
        }

        // Create TournamentPlayer entity
        TournamentPlayer tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setTournament(tournament);
        tournamentPlayer.setPlayerId(playerId);
        tournamentPlayer.setSignUpDateTime(LocalDateTime.now());
        tournamentPlayer.setStatus(TournamentPlayer.TournamentPlayerStatus.ACTIVE);

        tournament.getParticipants().add(tournamentPlayer);

        tournamentRepository.save(tournament);

        return ResponseEntity.status(HttpStatus.OK).body("Player successfully signed up to the tournament");
    }

    @Transactional
    public ResponseEntity<String> startTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        // Initialize the tournament's first round
        tournament.setCurrentRound(1);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);

        // Call MatchService to run matchmaking for the first round
        runMatchmaking(tournamentId);

        return ResponseEntity.status(HttpStatus.OK).body("Tournament started successfully.");
    }

    private void runMatchmaking(Long tournamentId) {
        webClientBuilder.build()
                .post()
                .uri(matchServiceUrl + "/api/v1/matches/matchmaking/" + tournamentId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Transactional
    public ResponseEntity<String> prepareNextRound(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with id: " + tournamentId));

        // Increment the round and save the tournament state
        if (tournament.getCurrentRound() < tournament.getMaxRounds()) {
            tournament.setCurrentRound(tournament.getCurrentRound() + 1);
        }

        tournamentRepository.save(tournament);

        // Call MatchService to either prepare the next round or finalize the tournament
        runPrepareNextRoundInMatchService(tournamentId);

        return ResponseEntity.status(HttpStatus.OK).body("Next round prepared successfully.");
    }

    private void runPrepareNextRoundInMatchService(Long tournamentId) {
        webClientBuilder.build()
                .post()
                .uri(matchServiceUrl + "/api/v1/matches/prepare-next-round/" + tournamentId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public TournamentDTO convertToDTO(Tournament tournament) {
        // Convert each TournamentPlayer to PlayerDTO using the correct fields
        Set<TournamentPlayerDTO> participantDTOs = tournament.getParticipants().stream()
                .map(player -> new TournamentPlayerDTO(
                        player.getId(),
                        player.getTournament().getId(), // Assuming you want to include the tournament ID
                        player.getPlayerId(),
                        player.getSignUpDateTime(),
                        player.getGlickoRating(),
                        player.getPoints(),
                        player.getRoundsPlayed(),
                        player.getStatus().name() // Convert enum to string
                ))
                .collect(Collectors.toSet());

        // Map the Tournament fields to TournamentDTO fields
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
                tournament.getFormat().name(), // Convert enum to string
                tournament.getStatus().name(), // Convert enum to string
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
                participantDTOs, // Pass the converted set of participant DTOs
                tournament.getTimeControlSetting()
        );
    }

    public PlayerDTO getPlayerDetails(Long playerId) {
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/" + playerId + "/details")
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();
    }
}