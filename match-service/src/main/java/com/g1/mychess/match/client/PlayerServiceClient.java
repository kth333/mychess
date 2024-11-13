package com.g1.mychess.match.client;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client for interacting with the Player service to update player profile ratings.
 */
@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    /**
     * Constructs a PlayerServiceClient with the specified base URL.
     *
     * @param playerServiceUrl the base URL of the Player service.
     * @param webClientBuilder a WebClient.Builder for creating a WebClient.
     */
    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    /**
     * Sends a request to update the player's rating profile.
     *
     * @param ratingUpdate the PlayerRatingUpdateDTO containing the updated player rating information.
     */
    public void updatePlayerProfileRating(PlayerRatingUpdateDTO ratingUpdate) {
        webClient.post()
                .uri("/api/v1/profile/rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /**
     * Retrieves the details of a player from the external player service.
     * This method uses the WebClient to make a GET request to the player service
     * to fetch information about the player based on their player ID.
     *
     * @param playerId The unique identifier of the player whose details are to be retrieved.
     * @return A {@link UserDTO} containing the player's details, such as name, email, and other relevant information.
     */
    public UserDTO getPlayerDetails(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/playerid/{playerId}", playerId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

}