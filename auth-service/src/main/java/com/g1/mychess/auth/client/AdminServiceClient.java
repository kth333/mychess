package com.g1.mychess.auth.client;

import com.g1.mychess.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service client to interact with the Admin Service API to fetch admin details.
 */
@Service
public class AdminServiceClient {
    private final WebClient webClient;

    /**
     * Constructor that initializes the WebClient with the provided Admin Service URL.
     *
     * @param adminServiceUrl the URL of the Admin Service (from application properties)
     * @param webClientBuilder the WebClient builder to create the WebClient instance
     */
    @Autowired
    public AdminServiceClient(@Value("${admin.service.url}") String adminServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(adminServiceUrl).build();
    }

    /**
     * Fetches an admin user by username from the Admin Service API.
     *
     * @param username the username of the admin to fetch
     * @return the UserDTO containing the admin's information
     */
    public UserDTO fetchAdminByUsername(String username) {
        return webClient.get()
                .uri("/api/v1/admin/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }
}
