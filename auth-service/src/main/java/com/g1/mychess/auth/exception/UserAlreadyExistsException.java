package com.g1.mychess.auth.exception;

/**
 * Exception thrown when a user already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified error message.
     *
     * @param message the error message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
