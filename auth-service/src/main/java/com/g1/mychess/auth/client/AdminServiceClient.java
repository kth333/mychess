package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AdminServiceClient {
    private final WebClient webClient;

    public AdminServiceClient(@Value("${admin.service.url}") String adminServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(adminServiceUrl).build();
    }

    public UserDTO fetchAdminByUsername(String username) {
        return webClient.get()
                .uri("/api/v1/admin/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }
}
