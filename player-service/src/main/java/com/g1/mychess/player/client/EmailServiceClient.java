package com.g1.mychess.player.client;

import com.g1.mychess.player.dto.ReportEmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service client for interacting with the external email service to send player report emails.
 * This class communicates with an external email service via HTTP requests using {@link WebClient}.
 * It provides a method to send a report email related to player activities or reports.
 */
@Service
public class EmailServiceClient {

    private final WebClient webClient;

    /**
     * Constructs a new {@link EmailServiceClient} with the specified email service URL and {@link WebClient} builder.
     * The provided email service URL is used as the base URL for making HTTP requests to the email service.
     *
     * @param emailServiceUrl the URL of the email service, specified in the application properties file
     * @param webClientBuilder the builder for creating the {@link WebClient} instance
     */
    public EmailServiceClient(@Value("${email.service.url}") String emailServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(emailServiceUrl).build();
    }

    /**
     * Sends a report email to the external email service with the provided report details.
     * This method makes a POST request to the email service's {@code /api/v1/email/reports} endpoint
     * with the {@link ReportEmailDTO} as the request body. It then blocks until the response is received.
     *
     * @param reportEmailDTO the data transfer object containing the details of the report email
     * @return a message indicating the result of the email sending operation
     */
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
