package com.g1.mychess.auth.exception;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified error message.
     *
     * @param message the error message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
