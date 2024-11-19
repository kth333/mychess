package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.MatchmakingDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A client service for managing matchmaking and tournament finalization operations.
 */
@Service
public class MatchServiceClient {

    private final WebClient webClient;

    /**
     * Constructs a new {@code MatchServiceClient} with the specified match service URL and WebClient builder.
     *
     * @param matchServiceUrl   the base URL of the match service
     * @param webClientBuilder  the WebClient builder used to configure the WebClient
     */
    public MatchServiceClient(@Value("${match.service.url}") String matchServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(matchServiceUrl).build();
    }

    /**
     * Initiates the matchmaking process for a specified tournament.
     *
     * @param matchmakingDTO the {@link MatchmakingDTO} containing the tournament and matchmaking details
     * @param jwtToken       the JWT token for authorization
     */
    public void runMatchmaking(MatchmakingDTO matchmakingDTO, String jwtToken) {
        webClient.post()
                .uri("/api/v1/matches/admin/{tournamentId}/matchmaking", matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /**
     * Finalizes a tournament by marking its status as completed.
     *
     * @param matchmakingDTO the {@link MatchmakingDTO} containing the tournament details
     * @param jwtToken       the JWT token for authorization
     */
    public void finalizeTournament(MatchmakingDTO matchmakingDTO, String jwtToken) {
        webClient.post()
                .uri("/api/v1/matches/admin/{tournamentId}/status/completed", matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
