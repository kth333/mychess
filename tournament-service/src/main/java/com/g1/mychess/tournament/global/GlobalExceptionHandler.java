package com.g1.mychess.tournament.global;

import com.g1.mychess.tournament.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle PlayerAlreadySignedUpException
    @ExceptionHandler(PlayerAlreadySignedUpException.class)
    public ResponseEntity<String> handlePlayerAlreadySignedUpException(PlayerAlreadySignedUpException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Handle PlayerBlacklistedException
    @ExceptionHandler(PlayerBlacklistedException.class)
    public ResponseEntity<String> handlePlayerBlacklistedException(PlayerBlacklistedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // Handle PlayerNotFoundException
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handle RequirementNotMetException
    @ExceptionHandler(RequirementNotMetException.class)
    public ResponseEntity<String> handleRequirementNotMetException(RequirementNotMetException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Handle TournamentAlreadyExistsException
    @ExceptionHandler(TournamentAlreadyExistsException.class)
    public ResponseEntity<String> handleTournamentAlreadyExistsException(TournamentAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle TournamentNotFound
    @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<String> handleTournamentNotFoundException(TournamentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PlayerNotSignedUpException.class)
    public ResponseEntity<String> handlePlayerNotSignedUpException(PlayerNotSignedUpException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handle any other exceptions (optional)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
