package com.g1.mychess.admin.client;

import com.g1.mychess.admin.dto.AdminPlayerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    public AdminPlayerDTO fetchPlayerDetails(Long playerId) {
        return webClient.get()
                .uri("/api/v1/player/{playerId}/admin-details", playerId)
                .retrieve()
                .bodyToMono(AdminPlayerDTO.class)
                .block();
    }

    public void updatePlayerBlacklistStatus(Long playerId) {
        webClient.put()
                .uri("/api/v1/player/blacklist-status/{playerId}", playerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void updatePlayerWhitelistStatus(Long playerId) {
        webClient.put()
                .uri("/api/v1/player/whitelist-status/{playerId}", playerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}