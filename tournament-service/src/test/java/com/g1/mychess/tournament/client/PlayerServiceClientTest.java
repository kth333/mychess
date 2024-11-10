package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class PlayerServiceClientTest {

    private MockWebServer mockWebServer;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private TournamentPlayerRepository tournamentPlayerRepository;

    private PlayerServiceClient playerServiceClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        playerServiceClient = new PlayerServiceClient(baseUrl, WebClient.builder());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetPlayerDetails() throws Exception {
        // Set up mock response with values matching the assertions
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\":1,\"username\":\"chessMaster\",\"age\":25,\"gender\":\"MALE\",\"glickoRating\":1500.0,\"ratingDeviation\":200.0,\"volatility\":0.06,\"blacklisted\":false}")
                .addHeader("Content-Type", "application/json"));

        // Execute the client call
        PlayerDTO player = playerServiceClient.getPlayerDetails(1L);

        // Assert the response matches the expected values
        assertNotNull(player);
        assertEquals(1L, player.getId());
        assertEquals(false, player.isBlacklisted());
        assertEquals("chessMaster", player.getUsername());
        assertEquals(25, player.getAge());
        assertEquals("MALE", player.getGender());
        assertEquals(1500.0, player.getGlickoRating());
        assertEquals(200.0, player.getRatingDeviation());
        assertEquals(0.06, player.getVolatility());
    }
}