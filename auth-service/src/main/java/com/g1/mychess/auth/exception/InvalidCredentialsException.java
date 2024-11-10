package com.g1.mychess.auth.exception;

/**
 * Exception thrown when the provided credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructs a new InvalidCredentialsException with the specified error message.
     *
     * @param message the error message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
