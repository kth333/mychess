package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player is not found in the system.
 */
public class PlayerNotFoundException extends RuntimeException {

    /**
     * Constructs a new PlayerNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
