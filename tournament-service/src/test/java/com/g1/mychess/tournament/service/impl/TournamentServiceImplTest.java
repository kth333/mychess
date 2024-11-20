package com.g1.mychess.tournament.service.impl;

import com.g1.mychess.tournament.client.EmailServiceClient;
import com.g1.mychess.tournament.client.MatchServiceClient;
import com.g1.mychess.tournament.client.PlayerServiceClient;
import com.g1.mychess.tournament.dto.MatchmakingDTO;
import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentNotificationDTO;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.mapper.TournamentMapper;
import com.g1.mychess.tournament.model.TimeControlSetting;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TournamentServiceImplTest {


    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentPlayerRepository tournamentPlayerRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PlayerServiceClient playerServiceClient;

    @Mock
    private MatchServiceClient matchServiceClient;

    @Mock
    private EmailServiceClient emailServiceClient;

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    /**
     * Sets up the testing environment before each test case is executed.
     *
     * This method mocks the `sendTournamentNotification` method of the
     * `emailServiceClient` to return an empty `Mono`. This ensures that the
     * email service interactions do not actually execute during tests,
     * isolating the tests from external dependencies.
     */

    @BeforeEach
    void setUp() {
        // Mock email service client to return a non-null Mono
        when(emailServiceClient.sendTournamentNotification(any(TournamentNotificationDTO.class)))
                .thenReturn(Mono.empty());
    }

    /**
     * Tests the successful creation of a tournament.
     *
     * This test prepares a TournamentDTO with valid tournament details,
     * mocks necessary repository and service methods, and verifies that
     * the tournament is created successfully. The test asserts that the
     * response status is HTTP 201 Created, the response body contains the
     * expected tournament details, and the saved tournament has the
     * correct attributes.
     */

    @Test
    public void testCreateTournament_Success() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setName("Test Tournament");
        tournamentDTO.setDescription("A test tournament");
        tournamentDTO.setMaxPlayers(16);
        tournamentDTO.setStartDateTime(LocalDateTime.now().plusDays(1));
        tournamentDTO.setEndDateTime(LocalDateTime.now().plusDays(2));
        tournamentDTO.setRegistrationStartDate(LocalDateTime.now());
        tournamentDTO.setRegistrationEndDate(LocalDateTime.now().plusHours(12));
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus("UPCOMING");
        tournamentDTO.setMinRating(1000);
        tournamentDTO.setMaxRating(2000);
        tournamentDTO.setAffectsRating(true);
        tournamentDTO.setMinAge(18);
        tournamentDTO.setMaxAge(60);
        tournamentDTO.setRequiredGender(null);
        tournamentDTO.setCountry("USA");
        tournamentDTO.setRegion("CA");
        tournamentDTO.setCity("San Francisco");
        tournamentDTO.setAddress("123 Chess St.");
        tournamentDTO.setMaxRounds(4);

        TimeControlSetting timeControlSetting = new TimeControlSetting(15, 10);
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        when(tournamentRepository.findByName(tournamentDTO.getName())).thenReturn(Optional.empty());
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);

        ArgumentCaptor<Tournament> tournamentCaptor = ArgumentCaptor.forClass(Tournament.class);
        when(tournamentRepository.save(tournamentCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<TournamentDTO> response = tournamentService.createTournament(tournamentDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        TournamentDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Tournament", responseBody.getName());

        Tournament savedTournament = tournamentCaptor.getValue();
        assertEquals(1L, savedTournament.getAdminId());
        assertEquals("Test Tournament", savedTournament.getName());
        assertEquals(timeControlSetting, savedTournament.getTimeControlSetting());
    }

    /**
     * Tests the scenario where a tournament is not found by its name.
     *
     * This test verifies that the service method `findTournamentByName` correctly throws a
     * `TournamentNotFoundException` when the given tournament name does not exist in the repository.
     *
     * Steps:
     * 1. Define a tournament name that does not exist.
     * 2. Mock the repository method `findByName` to return an empty `Optional`.
     * 3. Call the service method and assert that a `TournamentNotFoundException` is thrown.
     * 4. Verify that the exception message matches the expected message.
     *
     * Expected Outcome:
     * The test should pass if the `findTournamentByName` method throws a
     * `TournamentNotFound*/
    @Test
    void findTournamentByName_NotFound(){
        String tournamentName = "Non-Existent Tournament";

        when(tournamentRepository.findByName(tournamentName)).thenReturn(Optional.empty());

        Exception expectedException = assertThrows(TournamentNotFoundException.class, () ->
                tournamentService.findTournamentByName(tournamentName));

        assertEquals("Tournament not found with name: " + tournamentName, expectedException.getMessage());
    }

    /**
     * Tests the scenario where a tournament is not found by its ID.
     *
     * This test verifies that the service method `findTournamentById` correctly throws a
     * `TournamentNotFoundException` when the given tournament ID does not exist in the repository.
     *
     * Steps:
     * 1. Define a tournament ID that does not exist.
     * 2. Mock the repository method `findById` to return an empty `Optional`.
     * 3. Call the service method and assert that a `TournamentNotFoundException` is thrown.
     * 4. Verify that the exception message matches the expected message.
     *
     * Expected Outcome:
     * The test should pass if the `findTournamentById` method throws a `TournamentNotFoundException` with the correct message.
     */
    @Test
    void findTournamentById_NotFound(){
        long tournamentId = 999L;

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        Exception expectedException = assertThrows(TournamentNotFoundException.class, () ->
                tournamentService.findTournamentById(tournamentId));

        assertEquals("Tournament not found with id: " + tournamentId, expectedException.getMessage());
    }

    /**
     * Tests the successful scenario of signing up a player to a tournament.
     *
     * This test performs the following steps:
     * 1. Sets up a tournament with a valid registration period and rating range.
     * 2. Mocks tournament and player repository behaviors to return valid details.
     * 3. Constructs a PlayerDTO
     * */
    @Test
    void testSignUpToTournament_Success() {
        // Arrange
        long tournamentId = 1L;
        long playerId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setParticipants(new HashSet<>());
        tournament.setRegistrationStartDate(LocalDateTime.now().minusDays(1));
        tournament.setRegistrationEndDate(LocalDateTime.now().plusDays(1));
        tournament.setMinRating(1000);
        tournament.setMaxRating(2000);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(false);

        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                false, // isBlacklisted
                "playerUsername",
                "player@gmail.com",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );

        when(playerServiceClient.getPlayerDetails(playerId)).thenReturn(playerDTO);

        // Act
        ResponseEntity<String> response = tournamentService.signUpToTournament(tournamentId, playerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Player successfully signed up to the tournament", response.getBody());
    }

    /**
     * Tests the scenario where a player tries to sign up for a tournament but the eligibility
     * check fails because the player is blacklisted.
     *
     * This test does the following:
     * 1. Sets up a tournament with valid registration dates and initializes it with no participants.
     * 2. Mocks repository and service methods:
     *    - `tournamentRepository.findById` to return the tournament.
     *    - `tournamentPlayerRepository.existsByTournamentIdAndPlayerId` to return false, indicating the player hasn't signed up yet.
     *    - `playerServiceClient.getPlayerDetails` to return a PlayerDTO where the player is blacklisted.
     * 3. Calls the `signUpToTournament` service method and expects a `PlayerBlacklistedException` to be thrown.
     * 4. Asserts that the exception message matches the expected "Player is blacklisted from participating in tournaments."
     */
    @Test
    void testSignUpToTournament_PlayerEligibilityCheck_FailsDueToBlacklisted() {
        // Arrange
        long tournamentId = 1L;
        long playerId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setParticipants(new HashSet<>());
        tournament.setRegistrationStartDate(LocalDateTime.now().minusDays(1));
        tournament.setRegistrationEndDate(LocalDateTime.now().plusDays(1));

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(false);

        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                true, // isBlacklisted
                "playerUsername",
                "player@gmail.com",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );

        when(playerServiceClient.getPlayerDetails(playerId)).thenReturn(playerDTO);

        // Act & Assert
        PlayerBlacklistedException exception = assertThrows(PlayerBlacklistedException.class, () ->
                tournamentService.signUpToTournament(tournamentId, playerId)
        );

        assertEquals("Player is blacklisted from participating in tournaments.", exception.getMessage());
    }

    /**
     * Tests the scenario where a player tries to sign up for a tournament they are already signed up for.
     *
     * This test performs the following steps:
     * 1. Sets up a tournament with a valid registration period.
     * 2. Confirms that the player has already signed up for the tournament.
     * 3. Constructs a PlayerDTO with relevant player details.
     * 4. Calls the service method `signUpToTournament` and expects a `PlayerAlreadySignedUpException` to be thrown.
     * 5. Asserts that the exception message matches the expected "Player is already signed up for this tournament."
     *
     * Expected Outcome:
     * The test should pass if the `signUpToTournament` method throws a `PlayerAlreadySignedUpException` with the correct message.
     */
    @Test
    void testSignUpToTournament_AlreadySignedUp() {
        // Arrange
        long tournamentId = 1L;
        long playerId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setParticipants(new HashSet<>());
        tournament.setMinRating(1000); // Add this line
        tournament.setMaxRating(2000); // Add this line
        LocalDateTime registrationStartDate = LocalDateTime.now().minusDays(1); // registration opened
        tournament.setRegistrationStartDate(registrationStartDate);
        LocalDateTime registrationEndDate = LocalDateTime.now().plusDays(1);
        tournament.setRegistrationEndDate(registrationEndDate);     // registration closes tmr
        LocalDateTime now = LocalDateTime.now();

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(true);
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(true);

        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                false,
                "playerUsername",
                "email@domain.com",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );

        // Assert
        Exception expectedException = assertThrows(PlayerAlreadySignedUpException.class, () ->
                tournamentService.signUpToTournament(tournamentId, playerId));

        assertEquals("Player is already signed up for this tournament.", expectedException.getMessage());
    }

    /**
     * Tests the successful update of a tournament.
     *
     * This test performs the following steps:
     * 1. Sets up a TournamentDTO with updated tournament details.
     * 2. Mocks necessary repository and service methods to simulate the update scenario.
     * 3. Verifies that the tournament update process completes successfully.
     * 4. Asserts that the response status is HTTP 200 OK.
     * 5. Asserts that the updated tournament details in the response body are correct.
     */
    @Test
    public void testUpdateTournament_Success() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("Updated Tournament");
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus(Tournament.TournamentStatus.UPCOMING.name());

        TimeControlSetting timeControlSetting = new TimeControlSetting(30, 0);
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setAdminId(1L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<TournamentDTO> response = tournamentService.updateTournament(tournamentDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Tournament", response.getBody().getName());
        assertEquals(timeControlSetting, response.getBody().getTimeControlSetting());
    }

    /**
     * Tests the scenario where an attempt is made to update a non-existent tournament.
     *
     * This test performs the following steps:
     * 1. Sets up a `TournamentDTO` object with updated tournament details.
     * 2. Sets up a `Tournament` object to simulate the tournament to be updated.
     * 3. Mocks the repository method `findById` to return an empty `Optional`, indicating the tournament does not exist.
     * 4. Calls the `updateTournament` method and expects a `TournamentNotFoundException` to be thrown.
     * 5. Asserts that the exception message matches the expected message "Tournament not found with id: {id}".
     *
     * Expected Outcome:
     * The test should pass if the `updateTournament` method throws a `TournamentNotFoundException` with the correct message.
     */
    @Test
    public void testUpdateTournament_NotExist() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("Updated Tournament");
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus(Tournament.TournamentStatus.UPCOMING.name());

        TimeControlSetting timeControlSetting = new TimeControlSetting(30, 0); // 30 minutes base time, no increment
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setAdminId(1L);

        // Act
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        Exception expectedException = assertThrows(TournamentNotFoundException.class, () ->
                tournamentService.updateTournament(tournamentDTO, mockRequest));
        assertEquals("Tournament not found with id: " + tournamentDTO.getId(), expectedException.getMessage());
    }

    /**
     * Tests the scenario where an non-admin attempts to update a tournament.
     *
     * This test follows these steps:
     * 1. Prepares a `TournamentDTO` object with updated tournament details.
     * 2. Mocks the necessary repository and service interactions:
     *    - `tournamentRepository.findById` to return an existing tournament.
     *    - `authenticationService.getUserIdFromRequest` to simulate the user making the request.
     * 3. Calls the `updateTournament` method with the unauthorized user's details and expects an `UnauthorizedActionException` to be thrown.
     * 4. Asserts that the exception message matches the expected message "Only the tournament admin can perform this action."
     *
     * Expected Outcome:
     * The test should pass if the `updateTournament` method throws an `UnauthorizedActionException` with the correct message when an unauthorized user attempts to update a tournament.
     */
    @Test
    public void testUpdateTournament_Unauthorized() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("Updated Tournament");
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus(Tournament.TournamentStatus.UPCOMING.name());

        TimeControlSetting timeControlSetting = new TimeControlSetting(30, 0); // 30 minutes base time, no increment
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setAdminId(1L);

        long adminId = 999L;

        // Act
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(999L);

        // Assert
        Exception expectedException = assertThrows(UnauthorizedActionException.class, () ->
                tournamentService.updateTournament(tournamentDTO, mockRequest));
        assertEquals("Only the tournament admin can perform this action.", expectedException.getMessage());
    }

    /**
     * Tests the successful completion of a tournament.
     *
     * This test performs the following steps:
     * 1. Sets up a tournament with the status ONGOING and mocks the repository to return this tournament.
     * 2. Mocks the authentication service to simulate the admin user who is making the completion request.
     * 3. Calls the completeTournament method in the tournament service.
     * 4. Verifies that the HTTP response status is OK.
     * 5. Verifies that the response body contains the message "Tournament completed successfully."
     * 6. Asserts that the tournament status has been updated to COMPLETED.
     * 7. Verifies that the match service client is called to finalize the tournament.
     */
    @Test
    void testCompleteTournament_Success() {
        // Arrange
        long tournamentId = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setAdminId(1L);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournament.setParticipants(new HashSet<>());

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);
        when(authenticationService.extractJwtToken(mockRequest)).thenReturn("mock-jwt-token");

        // Act
        ResponseEntity<String> response = tournamentService.completeTournament(tournamentId, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tournament completed successfully.", response.getBody());
        assertEquals(Tournament.TournamentStatus.COMPLETED, tournament.getStatus());
        verify(matchServiceClient).finalizeTournament(any(MatchmakingDTO.class), eq("mock-jwt-token"));
    }


}