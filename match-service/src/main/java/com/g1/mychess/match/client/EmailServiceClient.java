package com.g1.mychess.match.client;
import com.g1.mychess.match.dto.ReminderEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailServiceClient {
    private final WebClient webClient;

    public EmailServiceClient(@Value("${player.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    public void sendMatchReminderEmail(ReminderEmailDTO reminderEmailDTO){
        webClient.post()
                .uri("/api/v1/email/matchreminder")
                .bodyValue(reminderEmailDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
