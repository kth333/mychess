package com.g1.mychess.match.client;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PlayerServiceClient {
    private final WebClient webClient;

    public PlayerServiceClient(@Value("${player.service.url}") String playerServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(playerServiceUrl).build();
    }

    public void updatePlayerProfileRating(PlayerRatingUpdateDTO ratingUpdate) {
        webClient.post()
                .uri("/api/v1/profile/rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}