package com.g1.mychess.tournament.client;

import com.g1.mychess.tournament.dto.TournamentNotificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * A client service for sending tournament-related email notifications.
 */
@Service
public class EmailServiceClient {

    private final WebClient webClient;

    /**
     * Constructs a new {@code EmailServiceClient} with the specified email service URL and WebClient builder.
     *
     * @param emailServiceUrl   the base URL of the email service
     * @param webClientBuilder  the WebClient builder used to configure the WebClient
     */
    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    /**
     * Sends a tournament notification email using the configured email service.
     *
     * @param notification the {@link TournamentNotificationDTO} containing the notification details
     * @return a {@link Mono} emitting a response string indicating success or failure
     */
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
