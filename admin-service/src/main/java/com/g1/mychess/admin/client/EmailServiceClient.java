package com.g1.mychess.admin.client;

import com.g1.mychess.admin.dto.BlacklistEmailDTO;
import com.g1.mychess.admin.dto.WhitelistEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailServiceClient {
    private final WebClient webClient;

    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    public void sendBlacklistNotificationEmail(BlacklistEmailDTO emailDTO) {
        webClient.post()
                .uri("/api/v1/email/blacklists")
                .bodyValue(emailDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void sendWhitelistNotificationEmail(WhitelistEmailDTO emailDTO) {
        webClient.post()
                .uri("/api/v1/email/whitelists")
                .bodyValue(emailDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}