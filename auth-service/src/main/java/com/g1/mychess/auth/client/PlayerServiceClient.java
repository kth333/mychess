package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.dto.PlayerCreationResponseDTO;
import com.g1.mychess.auth.dto.UpdatePasswordRequestDTO;
import com.g1.mychess.auth.dto.UserDTO;
import com.g1.mychess.auth.exception.PlayerServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO playerDTO) {
        return webClient.post()
                .uri("/api/v1/player/create")
                .bodyValue(playerDTO)
                .retrieve()
                .toEntity(PlayerCreationResponseDTO.class)
                .block();
    }

    public UserDTO fetchPlayerByUsername(String username) {
        return webClient.get()
                .uri("/api/v1/player/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public UserDTO fetchPlayerByEmail(String email) {
        return webClient.get()
                .uri("/api/v1/player/email/" + email)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public UserDTO fetchPlayerById(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/playerId/" + playerId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public void updatePassword(Long playerId, String hashedPassword) {
        UpdatePasswordRequestDTO updatePasswordRequest = new UpdatePasswordRequestDTO(playerId, hashedPassword);
        try {
            webClient.put()
                    .uri("/api/v1/player/update-password")
                    .bodyValue(updatePasswordRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new PlayerServiceException("Failed to update the user's password.");
        }
    }
}