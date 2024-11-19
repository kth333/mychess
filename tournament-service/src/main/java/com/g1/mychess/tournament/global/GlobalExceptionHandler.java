package com.g1.mychess.tournament.global;

import com.g1.mychess.tournament.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler to catch and handle specific exceptions in the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the PlayerAlreadySignedUpException.
     *
     * @param ex the exception
     * @return ResponseEntity with a CONFLICT status and the exception message
     */
    @ExceptionHandler(PlayerAlreadySignedUpException.class)
    public ResponseEntity<String> handlePlayerAlreadySignedUpException(PlayerAlreadySignedUpException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles the PlayerBlacklistedException.
     *
     * @param ex the exception
     * @return ResponseEntity with a FORBIDDEN status and the exception message
     */
    @ExceptionHandler(PlayerBlacklistedException.class)
    public ResponseEntity<String> handlePlayerBlacklistedException(PlayerBlacklistedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Handles the PlayerNotFoundException.
     *
     * @param ex the exception
     * @return ResponseEntity with a NOT_FOUND status and the exception message
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles the PlayerIneligibleException.
     *
     * @param ex the exception
     * @return ResponseEntity with a BAD_REQUEST status and the exception message
     */
    @ExceptionHandler(PlayerIneligibleException.class)
    public ResponseEntity<String> handleRequirementNotMetException(PlayerIneligibleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles the TournamentAlreadyExistsException.
     *
     * @param ex the exception
     * @return ResponseEntity with a CONFLICT status and the exception message
     */
    @ExceptionHandler(TournamentAlreadyExistsException.class)
    public ResponseEntity<String> handleTournamentAlreadyExistsException(TournamentAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles the TournamentNotFoundException.
     *
     * @param ex the exception
     * @return ResponseEntity with a NOT_FOUND status and the exception message
     */
    @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<String> handleTournamentNotFoundException(TournamentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles the PlayerNotSignedUpException.
     *
     * @param ex the exception
     * @return ResponseEntity with a NOT_FOUND status and the exception message
     */
    @ExceptionHandler(PlayerNotSignedUpException.class)
    public ResponseEntity<String> handlePlayerNotSignedUpException(PlayerNotSignedUpException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles the RegistrationPeriodException.
     *
     * @param ex the exception
     * @return ResponseEntity with a BAD_REQUEST status and the exception message
     */
    @ExceptionHandler(RegistrationPeriodException.class)
    public ResponseEntity<String> handleRegistrationPeriodException(RegistrationPeriodException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles the UnauthorizedActionException.
     *
     * @param ex the exception
     * @return ResponseEntity with a FORBIDDEN status and the exception message
     */
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleRegistrationPeriodException(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Handles any other unhandled exceptions.
     *
     * @param ex the exception
     * @return ResponseEntity with an INTERNAL_SERVER_ERROR status and the exception message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
