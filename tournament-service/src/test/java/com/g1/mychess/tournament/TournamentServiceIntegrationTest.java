package com.g1.mychess.tournament;

import com.g1.mychess.tournament.dto.PlayerDTO;

import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.impl.TournamentServiceImpl;
import com.g1.mychess.tournament.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TournamentServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@WithMockUser(username = "testAdmin", roles = "ROLES_ADMIN")
public class TournamentServiceIntegrationTest {
    // @MockBean services

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private TournamentPlayerRepository tournamentPlayerRepository;

    @Autowired
    JwtUtil jwtUtil;

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    WebTestClient webTestClient;  // Autoconfigured WebTestClient

    @Mock
    HttpServletRequest mockRequest;

    String token = "";

    @BeforeEach
    void setup(){
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testAdmin")
                .password("password") // Password is not used in this case
                .roles("ADMIN")
                .build();

        // Generate the JWT token
        token = jwtUtil.generateToken(userDetails, 1L);

        if (token==null){
            throw new RuntimeException("Could not generate token");
        }

    }


//    ationImpl{interpolatedMessage='must be greater than or equal to 1', propertyPath=maxRounds, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.Min.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=startDateTime, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=status, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=address, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=format, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=region, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=country, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=registrationStartDate, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=registrationEndDate, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=adminId, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=endDateTime, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=maxAge, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=description, rootBeanClass=class com.g1.mychess.tournament.model.Tournament, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
//    ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=minAge, rootBeanClass=class com.g1.mychess

    @AfterEach
    void tearDown() {
        tournamentRepository.deleteAll();
        tournamentPlayerRepository.deleteAll();
    }


//    @Test
//    void findTournamentById_Success() {
//        Tournament tournament = new Tournament();
//        tournament.setName("Test-Tournament");
//        tournament.setMaxPlayers(100);
//        tournament.setMaxRounds(1);
//        tournament.setStartDateTime(new LocalDate(1,1,2024),new LocalTime(0,0,0,0));
//        // Set other necessary fields...
//
//        tournamentRepository.save(tournament);
//
//        // Perform the HTTP request using WebTestClient
//        webTestClient.get().uri(baseUrl+port+"/api/v1/tournaments", tournament.getId())
//                .exchange()
//                .expectStatus().isOk() // Expect 200 OK status
//                .expectBody() // Expect the body to contain the correct data
//                .jsonPath("$.name").isEqualTo("Test Tournament")
//                .jsonPath("$.maxPlayers").isEqualTo(100);
//    }


}
