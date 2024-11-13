package com.g1.mychess.match.global;

import com.g1.mychess.match.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for handling exceptions across the entire application.
 * This class provides specific responses for different types of custom exceptions
 * and a general response for unhandled exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles MatchNotFoundException by returning a 404 NOT FOUND response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing the exception message and HTTP status NOT FOUND.
     */
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<String> handleMatchNotFoundException(MatchNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles PlayerNotFoundException by returning a 404 NOT FOUND response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing the exception message and HTTP status NOT FOUND.
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles TournamentNotFoundException by returning a 404 NOT FOUND response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing the exception message and HTTP status NOT FOUND.
     */
    @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<String> handleTournamentNotFoundException(TournamentNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles TournamentRoundNotFoundException by returning a 404 NOT FOUND response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing the exception message and HTTP status NOT FOUND.
     */
    @ExceptionHandler(TournamentRoundNotFoundException.class)
    public ResponseEntity<String> handleTournamentRoundNotFoundException(TournamentRoundNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UnauthorizedActionException by returning a 403 FORBIDDEN response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing the exception message and HTTP status FORBIDDEN.
     */
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles all other exceptions by returning a 500 INTERNAL SERVER ERROR response.
     *
     * @param ex the exception instance.
     * @param request the current request.
     * @return a ResponseEntity containing a generic error message and HTTP status INTERNAL SERVER ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
