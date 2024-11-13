package com.g1.mychess.player.client;

import com.g1.mychess.player.dto.ReportEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailServiceClient {

    private final WebClient webClient;

    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    public String sendPlayerReportEmail(ReportEmailDTO reportEmailDTO) {
        try {
            return webClient.post()
                    .uri("/api/v1/email/reports")
                    .bodyValue(reportEmailDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking call to make the method synchronous
        } catch (Exception e) {
            System.err.println("Error sending report email: " + e.getMessage());
            return "Failed to send report email";
        }
    }
}