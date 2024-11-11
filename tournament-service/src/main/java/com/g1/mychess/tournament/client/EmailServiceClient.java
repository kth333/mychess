package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.TournamentNotificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailServiceClient {

    private final WebClient webClient;

    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    public Mono<String> sendTournamentNotification(TournamentNotificationDTO notification) {
        return webClient.post()
                .uri("/api/v1/email/tournament-notification")
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    // Log the error or handle fallback
                    System.err.println("Error sending tournament notification: " + error.getMessage());
                    return Mono.just("Failed to send notification");
                });
    }
}