package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player attempts to sign up for a tournament they are already signed up for.
 */
public class PlayerAlreadySignedUpException extends RuntimeException {

    /**
     * Constructs a new PlayerAlreadySignedUpException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlayerAlreadySignedUpException(String message) {
        super(message);
    }
}
