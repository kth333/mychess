package com.g1.mychess.tournament.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.util.JwtUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class PlayerServiceClientIntegrationTest {

    private MockWebServer mockWebServer;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private TournamentPlayerRepository tournamentPlayerRepository;

    @MockBean
    private JwtUtil jwtUtil;

    private PlayerServiceClient playerServiceClient;

    private static final String baseUrl = "/api/v1/player/";

    /**
     * This is an integration test focusing on the PlayerServiceClient
     * This tests the end-points.
     * The test environment is a MockWebServer and a real PlayerServiceClient instance.
     *
     * @throws IOException if an I/O error occurs when starting the MockWebServer
     */
    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.url(baseUrl);
        playerServiceClient = new PlayerServiceClient(mockWebServer.url(baseUrl).toString(), WebClient.builder());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * Tests the `getPlayerDetails` method of the `PlayerServiceClient` class.
     *
     * This test verifies that the `getPlayerDetails` method returns the expected player details
     * for a given player ID. The test sets up a mock server response, executes the client call,
     * and asserts that the returned `PlayerDTO` object matches the expected values.
     *
     * @throws Exception if any errors occur during test execution.
     */
    @Test
    public void testGetPlayerDetails() throws Exception {
        long playerId = 1L;

        // Create a PlayerDTO object
        PlayerDTO playerDTO = new PlayerDTO(playerId,false,"chessMaster",
                "email@domain.com", 25,"MALE",1500.0,200.0,0.06);

        // Serialize PlayerDTO to JSON using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String playerJson = objectMapper.writeValueAsString(playerDTO);

        mockWebServer.url(baseUrl + playerId + "/details");
        // Set up mock response with values matching the assertions
        mockWebServer.enqueue(new MockResponse()
                .setBody(playerJson)
                .addHeader("Content-Type", "application/json"));

        // Execute the client call
        PlayerDTO result = playerServiceClient.getPlayerDetails(1L);

        // Assert the response matches the expected values
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(false, result.isBlacklisted());
        assertEquals("chessMaster", result.getUsername());
        assertEquals(25, result.getAge());
        assertEquals("MALE", result.getGender());
        assertEquals(1500.0, result.getGlickoRating());
        assertEquals(200.0, result.getRatingDeviation());
        assertEquals(0.06, result.getVolatility());
    }

    @Test
    void test_GetNewPlayer_NotExistingPlayer() throws Exception {

        Long playerId = 999L;
        mockWebServer.url(baseUrl + playerId + "/details");

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Player not found"));

        // Assert the response matches the expected values
        assertThrows(WebClientResponseException.NotFound.class, () -> playerServiceClient.getPlayerDetails(playerId));

    }
}

