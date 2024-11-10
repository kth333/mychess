package com.g1.mychess.auth.exception;

/**
 * Exception thrown when a password is invalid.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs a new InvalidPasswordException with the specified error message.
     *
     * @param message the error message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
