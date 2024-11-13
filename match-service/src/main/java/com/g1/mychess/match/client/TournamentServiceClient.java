package com.g1.mychess.match.client;

import com.g1.mychess.match.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client for fetching tournament details from an external Tournament service.
 */
@Service
public class TournamentServiceClient {

    private final WebClient webClient;

    /**
     * Constructs a TournamentServiceClient with the specified base URL.
     *
     * @param tournamentServiceUrl the base URL of the Tournament service.
     * @param webClientBuilder a WebClient.Builder for creating a WebClient.
     */
    public TournamentServiceClient(@Value("${tournament.service.url}") String tournamentServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(tournamentServiceUrl).build();
    }

    /**
     * Retrieves the details of a tournament by its ID.
     *
     * @param tournamentId the ID of the tournament to retrieve.
     * @return the details of the tournament as a TournamentDTO.
     */
    public TournamentDTO getTournamentDetails(Long tournamentId) {
        return webClient.get()
                .uri("/api/v1/tournaments/id/{tournamentId}", tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }
}