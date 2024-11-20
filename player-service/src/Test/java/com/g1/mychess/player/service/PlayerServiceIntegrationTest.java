package com.g1.mychess.player.service;

import com.g1.mychess.player.PlayerServiceApplication;
import com.g1.mychess.player.dto.PlayerCreationResponseDTO;
import com.g1.mychess.player.dto.PlayerDTO;
import com.g1.mychess.player.dto.RegisterRequestDTO;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.impl.PlayerRatingHistoryServiceImpl;
import com.g1.mychess.player.service.impl.PlayerServiceImpl;
import com.g1.mychess.player.service.impl.ProfileServiceImpl;
import com.g1.mychess.player.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = PlayerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@WithMockUser(username = "testAdmin", roles = "ROLES_ADMIN")
public class PlayerServiceIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private PlayerRatingHistoryServiceImpl playerRatingHistoryService;
    @Autowired
    private ProfileServiceImpl profileService;
    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PlayerRatingHistoryRepository playerRatingHistoryRepository;


    @Autowired
    JwtUtil jwtUtil;

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    String token = "";

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testAdmin")
                .password("password") // Password is not used in this case
                .roles("ADMIN")
                .build();

        // Generate the JWT token
        token = jwtUtil.generateToken(userDetails, 1L);
    }

    @AfterEach
    public void tearDown() {
        playerRepository.deleteAll();
        profileRepository.deleteAll();
    }

    @Test
    void test_createPlayer_Success() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("username");
        registerRequestDTO.setEmail("email@domain.com");
        registerRequestDTO.setPassword("password123");
        registerRequestDTO.setGender("Male");
        registerRequestDTO.setBirthDate(LocalDate.now().minusYears(20));
        registerRequestDTO.setCity("Singapore");
        registerRequestDTO.setCountry("Singapore");
        registerRequestDTO.setRegion("Central");

        Profile profile = new Profile();
        profile.setCity("Singapore");
        profile.setCountry("Singapore");
        profile.setRegion("Central");
        profile.setFullName("Full Name");
        profile.setBio("This is a test bio.");
        profile.setGender("Male");
        profile.setCountry("Singapore");
        profile.setRegion("Singapore");
        profile.setCity("Singapore");
        profile.setBirthDate(LocalDate.of(1990, 1, 1)); // Set a sample birth date
        profile.setGlickoRating(1600.0);
        profile.setRatingDeviation(300.0);
        profile.setVolatility(0.05);
        profile.setTotalWins(10);
        profile.setTotalLosses(5);
        profile.setTotalDraws(2);
        profile.setLastActive(LocalDate.now().minusDays(1)); // Last active as yesterday
        profile.setPublic(true);

        // Optionally, add sample match history or tournament history
        profile.setMatchHistory(new ArrayList<>());
        profile.setTournamentHistory(new ArrayList<>());

        Player player = new Player();
        player.setUsername("username");
        player.setEmail("email@domain.com");
        player.setPassword("password123");
        player.setRole("USER");
        player.setPlayerId(1L);
        player.setBlacklisted(false);
        player.setJoinedDate(LocalDate.now());

        profile.setPlayer(player);
        player.setProfile(profile);

        profileRepository.save(profile); // Save profile first
        playerRepository.save(player);   // Save player, which cascades the profile save


    }

    @Test
    void testPutPlayerDetails(){
        long playerId = 1L;
        URI uri = URI.create(baseUrl + port + "/api/v1/player/" + playerId + "/details");

        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                false,
                "playerUsername",
                "local@domain.com",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );


        webTestClient.put()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .bodyValue(playerDTO) // add dto to the request body
                .exchange() // Send the request and getting the response
                .expectStatus().isOk() // request is 200 OK
                .expectBody() // Verifying the body
                .jsonPath("$.id").isEqualTo(playerId); // you can assert the player ID is returned
    }

    @Test
    void testGetPlayerDetails() {
        long playerId = 1L;

        URI uri = URI.create(baseUrl + port + "/api/v1/player/" + playerId + "/details");

        webTestClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDTO.class)
                .value(playerDTO -> {
                    assertNotNull(playerDTO);
                    assertEquals(1L, playerDTO.getId());
                });
    }

}
