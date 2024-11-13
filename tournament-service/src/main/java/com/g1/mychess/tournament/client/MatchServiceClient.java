package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.MatchmakingDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MatchServiceClient {
    private final WebClient webClient;

    public MatchServiceClient(@Value("${match.service.url}") String matchServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(matchServiceUrl).build();
    }

    public void runMatchmaking(MatchmakingDTO matchmakingDTO, String tournamentFormat, String jwtToken) {
        webClient.post()
                .uri("/api/v1/matches/admin/{tournamentId}/matchmaking", matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void finalizeTournament(MatchmakingDTO matchmakingDTO, String jwtToken) {
        webClient.post()
                .uri("/api/v1/matches/admin/{tournamentId}/status/completed", matchmakingDTO.getTournamentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(matchmakingDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}