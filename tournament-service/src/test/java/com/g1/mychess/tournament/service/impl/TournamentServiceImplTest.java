package com.g1.mychess.tournament.service.impl;


import com.g1.mychess.tournament.exception.PlayerAlreadySignedUpException;
import com.g1.mychess.tournament.exception.TournamentNotFoundException;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceImplTest {

    @InjectMocks
    TournamentServiceImpl tournamentService;

    @Mock
    TournamentRepository tournamentRepository;
    @Mock
    TournamentPlayerRepository tournamentPlayerRepository;
    @Mock
    WebClient.Builder webClientBuilder;
    @Mock
    JwtUtil jwtUtil;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);  // Initialize mocks
//        Handled by @InjectMocks and openMock(this);
//        tournamentService = new TournamentServiceImpl(tournamentRepository,tournamentPlayerRepository,jwtUtil,webClientBuilder);

    }

    @Test
    void signUpToTournament_InvalidTournamentID_Should_Throw_Exception(){
        // arrange
        long invalidTournamentID = 99L;
        long playerID = 1L;

        // Nothing else to prepare since no tournament will be returned
//        when(tournamentRepository.findById(invalidTournamentID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TournamentNotFoundException.class, () -> tournamentService.signUpToTournament(invalidTournamentID, playerID));

        assertEquals("Tournament not found with id: " + invalidTournamentID, exception.getMessage());

    }

    @Test
    void signUpToTournament_PlayerAlreadySignedUp_ShouldThrow_Exception(){
        // Arrange
        long tournamentID = 1L;
        long playerID = 99L;

        Tournament tournament = mock(Tournament.class);

        // Act, Stubs
        when(tournamentRepository.findById(tournamentID)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentID, playerID)).thenReturn(true);

        // Assert
        Exception exception = assertThrows(PlayerAlreadySignedUpException.class, () -> tournamentService.signUpToTournament(tournamentID, playerID));
        assertEquals("Player is already signed up for this tournament.", exception.getMessage());

    }

    @Test
    void signUpToTournament_PlayerAlreadySignedUp_ShouldThrow_Exception2() {
        // Arrange
        long tournamentID = 1L;
        long playerID = 99L;

        Optional<Tournament> tournament = mock(Optional.class);

        // Stub the repository to return the tournament when queried by ID
        when(tournamentRepository.findById(tournamentID)).thenReturn(tournament);
        // Stub to indicate the player is already signed up
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentID, playerID)).thenReturn(true);

        // Act & Assert
        PlayerAlreadySignedUpException exception = assertThrows(PlayerAlreadySignedUpException.class, () -> {
            tournamentService.signUpToTournament(tournamentID, playerID);
        });

        // Verify the exception message
        assertEquals("Player is already signed up for this tournament.", exception.getMessage());
    }
}