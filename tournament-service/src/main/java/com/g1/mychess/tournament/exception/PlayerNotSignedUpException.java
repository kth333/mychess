package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player attempts to perform an action without being signed up for the tournament.
 */
public class PlayerNotSignedUpException extends RuntimeException {

    /**
     * Constructs a new PlayerNotSignedUpException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlayerNotSignedUpException(String message) {
        super(message);
    }
}
