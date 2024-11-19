package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A client service for retrieving player details.
 */
@Service
public class PlayerServiceClient {

    private final WebClient webClient;

    /**
     * Constructs a new {@code PlayerServiceClient} with the specified player service URL and WebClient builder.
     *
     * @param playerServiceUrl  the base URL of the player service
     * @param webClientBuilder  the WebClient builder used to configure the WebClient
     */
    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    /**
     * Retrieves the details of a player by their ID.
     *
     * @param playerId the ID of the player
     * @return a {@link PlayerDTO} containing the player's details
     */
    public PlayerDTO getPlayerDetails(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/{playerId}/details", playerId)
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();
    }
}
