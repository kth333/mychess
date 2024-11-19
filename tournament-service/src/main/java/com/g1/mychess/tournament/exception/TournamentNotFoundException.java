package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a tournament with the specified ID is not found.
 */
public class TournamentNotFoundException extends RuntimeException {

    /**
     * Constructs a new TournamentNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public TournamentNotFoundException(String message) {
        super(message);
    }
}
