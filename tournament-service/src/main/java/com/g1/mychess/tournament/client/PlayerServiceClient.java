package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    public PlayerDTO getPlayerDetails(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/{playerId}/details", playerId)
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();
    }
}