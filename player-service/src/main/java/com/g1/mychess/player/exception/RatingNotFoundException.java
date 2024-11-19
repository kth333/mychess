package com.g1.mychess.player.exception;

/**
 * Custom exception class to handle scenarios where a rating is not found.
 * This exception extends RuntimeException and is thrown when a requested
 * rating cannot be found in the system.
 */
public class RatingNotFoundException extends RuntimeException {

    /**
     * Constructs a new RatingNotFoundException with the specified detail message.
     *
     * @param message the detail message to be passed with the exception
     */
    public RatingNotFoundException(String message) {
        super(message);
    }
}