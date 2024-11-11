package com.g1.mychess.match.client;

import com.g1.mychess.match.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TournamentServiceClient {

    private final WebClient webClient;

    public TournamentServiceClient(@Value("${tournament.service.url}") String tournamentServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(tournamentServiceUrl).build();
    }

    public TournamentDTO getTournamentDetails(Long tournamentId) {
        return webClient.get()
                .uri("/api/v1/tournaments/public/id/{tournamentId}", tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }
}