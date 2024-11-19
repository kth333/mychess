package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player who is blacklisted attempts to participate in a tournament.
 */
public class PlayerBlacklistedException extends RuntimeException {

    /**
     * Constructs a new PlayerBlacklistedException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlayerBlacklistedException(String message) {
        super(message);
    }
}
