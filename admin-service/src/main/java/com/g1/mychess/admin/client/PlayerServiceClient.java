package com.g1.mychess.admin.client;

import com.g1.mychess.admin.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client service for interacting with the Player Service API.
 * Provides methods to retrieve player details and update their blacklist/whitelist status.
 */
@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    /**
     * Constructor to initialize PlayerServiceClient with the base URL of the player service
     * and a WebClient.Builder instance.
     *
     * @param playerServiceUrl the URL of the player service API
     * @param webClientBuilder the WebClient.Builder instance to build the WebClient
     */
    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();  // Set the base URL of the player service
    }

    /**
     * Retrieves the details of a player by their player ID.
     *
     * @param playerId the ID of the player whose details are to be fetched
     * @return a PlayerDTO containing the details of the player
     */
    public PlayerDTO getPlayerDetails(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/{playerId}/details", playerId)  // URI for getting player details
                .retrieve()
                .bodyToMono(PlayerDTO.class)  // Convert response body to PlayerDTO
                .block();  // Block and wait for the response
    }

    /**
     * Updates the blacklist status of a player by their player ID.
     *
     * @param playerId the ID of the player whose blacklist status is to be updated
     */
    public void updatePlayerBlacklistStatus(Long playerId) {
        webClient.put()
                .uri("/api/v1/player/blacklist-status/{playerId}", playerId)  // URI for updating blacklist status
                .retrieve()
                .toBodilessEntity()  // No response body expected
                .block();  // Block and wait for the update to complete
    }

    /**
     * Updates the whitelist status of a player by their player ID.
     *
     * @param playerId the ID of the player whose whitelist status is to be updated
     */
    public void updatePlayerWhitelistStatus(Long playerId) {
        webClient.put()
                .uri("/api/v1/player/whitelist-status/{playerId}", playerId)  // URI for updating whitelist status
                .retrieve()
                .toBodilessEntity()  // No response body expected
                .block();  // Block and wait for the update to complete
    }
}