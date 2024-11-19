package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when an admin attempts to create a tournament that already exists.
 */
public class TournamentAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new TournamentAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public TournamentAlreadyExistsException(String message) {
        super(message);
    }
}
