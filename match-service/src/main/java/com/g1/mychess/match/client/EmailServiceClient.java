package com.g1.mychess.match.client;
import com.g1.mychess.match.dto.ReminderEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A service client for sending match reminder emails via an external email service.
 * This client communicates with the email service through WebClient to send email reminders
 * for upcoming matches in the chess tournament.
 */
@Service
public class EmailServiceClient {
    private final WebClient webClient;

    /**
     * Constructs an instance of the EmailServiceClient with the specified email service URL.
     * The URL is injected through the application configuration properties.
     *
     * @param emailServiceUrl The base URL of the email service, configured in application properties.
     * @param webClientBuilder A builder for the WebClient used to make HTTP requests to the email service.
     */
    public EmailServiceClient(@Value("${player.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    /**
     * Sends a match reminder email using the external email service.
     * This method takes a {@link ReminderEmailDTO} object that contains the necessary information
     * (e.g., recipient, tournament name, scheduled match time) to send a reminder email to a player.
     *
     * @param reminderEmailDTO A DTO containing the details of the match reminder email.
     * @see ReminderEmailDTO
     */
    public void sendMatchReminderEmail(ReminderEmailDTO reminderEmailDTO){
        webClient.post()
                .uri("/api/v1/email/matchreminder")
                .bodyValue(reminderEmailDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
