package com.g1.mychess.player.exception;

/**
 * Custom exception class to handle scenarios where a player is not found.
 * This exception extends RuntimeException and is thrown when a requested
 * player cannot be found in the system.
 */
public class PlayerNotFoundException extends RuntimeException {

    /**
     * Constructs a new PlayerNotFoundException with the specified detail message.
     *
     * @param message the detail message to be passed with the exception
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
