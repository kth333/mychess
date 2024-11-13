package com.g1.mychess.admin.client;

import com.g1.mychess.admin.dto.BlacklistEmailDTO;
import com.g1.mychess.admin.dto.WhitelistEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client service for interacting with the Email Service API.
 * Provides methods to send notification emails for blacklist and whitelist actions.
 */
@Service
public class EmailServiceClient {
    private final WebClient webClient;

    /**
     * Constructor to initialize EmailServiceClient with the base URL of the email service
     * and a WebClient.Builder instance.
     *
     * @param emailServiceUrl the URL of the email service API
     * @param webClientBuilder the WebClient.Builder instance to build the WebClient
     */
    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();  // Set the base URL of the email service
    }

    /**
     * Sends a notification email when a player is blacklisted.
     *
     * @param emailDTO the BlacklistEmailDTO containing the email details and information
     */
    public void sendBlacklistNotificationEmail(BlacklistEmailDTO emailDTO) {
        webClient.post()
                .uri("/api/v1/email/blacklists")  // URI for sending blacklist notification
                .bodyValue(emailDTO)  // Send the emailDTO as the body of the request
                .retrieve()
                .toBodilessEntity()  // No response body expected
                .block();  // Block and wait for the email to be sent
    }

    /**
     * Sends a notification email when a player is whitelisted.
     *
     * @param emailDTO the WhitelistEmailDTO containing the email details and information
     */
    public void sendWhitelistNotificationEmail(WhitelistEmailDTO emailDTO) {
        webClient.post()
                .uri("/api/v1/email/whitelists")  // URI for sending whitelist notification
                .bodyValue(emailDTO)  // Send the emailDTO as the body of the request
                .retrieve()
                .toBodilessEntity()  // No response body expected
                .block();  // Block and wait for the email to be sent
    }
}