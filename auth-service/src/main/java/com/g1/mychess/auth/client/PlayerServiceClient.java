package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.dto.PlayerCreationResponseDTO;
import com.g1.mychess.auth.dto.UpdatePasswordRequestDTO;
import com.g1.mychess.auth.dto.UserDTO;
import com.g1.mychess.auth.exception.PlayerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service client to interact with the Player Service API for managing player data.
 */
@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    /**
     * Constructor that initializes the WebClient with the provided Player Service URL.
     *
     * @param playerServiceUrl the URL of the Player Service (from application properties)
     * @param webClientBuilder the WebClient builder to create the WebClient instance
     */
    @Autowired
    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    /**
     * Creates a new player in the Player Service.
     *
     * @param playerDTO the player registration data
     * @return a ResponseEntity containing the response from the Player Service
     */
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO playerDTO) {
        return webClient.post()
                .uri("/api/v1/player/")
                .bodyValue(playerDTO)
                .retrieve()
                .toEntity(PlayerCreationResponseDTO.class)
                .block();
    }

    /**
     * Fetches a player by their username from the Player Service.
     *
     * @param username the username of the player
     * @return the UserDTO containing the player's data
     */
    public UserDTO fetchPlayerByUsername(String username) {
        return webClient.get()
                .uri("/api/v1/player/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    /**
     * Fetches a player by their email from the Player Service.
     *
     * @param email the email of the player
     * @return the UserDTO containing the player's data
     */
    public UserDTO fetchPlayerByEmail(String email) {
        return webClient.get()
                .uri("/api/v1/player/email/" + email)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    /**
     * Fetches a player by their ID from the Player Service.
     *
     * @param playerId the ID of the player
     * @return the UserDTO containing the player's data
     */
    public UserDTO fetchPlayerById(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/playerId/" + playerId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    /**
     * Updates the password for a player in the Player Service.
     *
     * @param playerId      the ID of the player whose password is being updated
     * @param hashedPassword the new hashed password
     * @throws PlayerServiceException if there is an error updating the password
     */
    public void updatePassword(Long playerId, String hashedPassword) {
        UpdatePasswordRequestDTO updatePasswordRequest = new UpdatePasswordRequestDTO(playerId, hashedPassword);
        try {
            webClient.put()
                    .uri("/api/v1/player/password")
                    .bodyValue(updatePasswordRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new PlayerServiceException("Failed to update the user's password.");
        }
    }
}
