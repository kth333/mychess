package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player attempts to participate in a tournament but is ineligible.
 */
public class PlayerIneligibleException extends RuntimeException {

    /**
     * Constructs a new PlayerIneligibleException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlayerIneligibleException(String message) {
        super(message);
    }
}
