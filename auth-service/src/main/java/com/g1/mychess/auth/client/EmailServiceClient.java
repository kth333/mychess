package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.EmailRequestDTO;
import com.g1.mychess.auth.exception.EmailSendFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailServiceClient {
    private final WebClient webClient;

    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

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

    public void sendPasswordResetEmail(EmailRequestDTO emailRequestDTO) {
        try {
            webClient.post()
                    .uri("/api/v1/email/password-resets")
                    .bodyValue(emailRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send the password reset email.");
        }
    }
}