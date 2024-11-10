package com.g1.mychess.auth.exception;

/**
 * Exception thrown when there is an error in the player service.
 */
public class PlayerServiceException extends RuntimeException {

    /**
     * Constructs a new PlayerServiceException with the specified error message.
     *
     * @param message the error message
     */
    public PlayerServiceException(String message) {
        super(message);
    }
}
