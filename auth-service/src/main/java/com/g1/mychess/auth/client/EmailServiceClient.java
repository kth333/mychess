package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.EmailRequestDTO;
import com.g1.mychess.auth.exception.EmailSendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service client to interact with the Email Service API to send emails.
 */
@Service
public class EmailServiceClient {
    private final WebClient webClient;

    /**
     * Constructor that initializes the WebClient with the provided Email Service URL.
     *
     * @param emailServiceUrl the URL of the Email Service (from application properties)
     * @param webClientBuilder the WebClient builder to create the WebClient instance
     */
    @Autowired
    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    /**
     * Sends a verification email using the Email Service API.
     *
     * @param emailRequestDTO the request data for sending the verification email
     * @throws EmailSendFailedException if the email sending fails
     */
    public void sendVerificationEmail(EmailRequestDTO emailRequestDTO) {
        try {
            webClient.post()
                    .uri("/api/v1/email/verification")
                    .bodyValue(emailRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send the verification email.");
        }
    }

    /**
     * Sends a password reset email using the Email Service API.
     *
     * @param emailRequestDTO the request data for sending the password reset email
     * @throws EmailSendFailedException if the email sending fails
     */
    public void sendPasswordResetEmail(EmailRequestDTO emailRequestDTO) {
        try {
            webClient.post()
                    .uri("/api/v1/email/password-recovery")
                    .bodyValue(emailRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send the password reset email.");
        }
    }
}
