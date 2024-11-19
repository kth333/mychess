package com.g1.mychess.player.global;

import com.g1.mychess.player.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler is a centralized exception handler class that manages
 * the handling of various exceptions throughout the application. It uses
 * Spring's @ControllerAdvice to handle exceptions globally and return appropriate
 * HTTP responses for each exception type.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PlayerNotFoundException and returns an HTTP response with a NOT_FOUND status.
     *
     * @param ex the PlayerNotFoundException that was thrown
     * @return a ResponseEntity with HTTP status 404 (NOT_FOUND) and the exception message
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        // Return a response entity with NOT_FOUND status and the exception message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles RatingNotFoundException and returns an HTTP response with a NOT_FOUND status.
     *
     * @param ex the RatingNotFoundException that was thrown
     * @return a ResponseEntity with HTTP status 404 (NOT_FOUND) and the exception message
     */
    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<String> handleRatingNotFoundException(RatingNotFoundException ex) {
        // Return a response entity with NOT_FOUND status and the exception message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles generic exceptions and returns an HTTP response with an INTERNAL_SERVER_ERROR status.
     * This handler catches any unexpected exceptions and provides a generic error message.
     *
     * @param ex the Exception that was thrown
     * @return a ResponseEntity with HTTP status 500 (INTERNAL_SERVER_ERROR) and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        // Return a generic error response with INTERNAL_SERVER_ERROR status
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}