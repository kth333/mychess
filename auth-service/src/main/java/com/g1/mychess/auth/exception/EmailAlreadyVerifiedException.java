package com.g1.mychess.auth.exception;

/**
 * Exception thrown when a user attempts to verify an email that is already verified.
 */
public class EmailAlreadyVerifiedException extends RuntimeException {

    /**
     * Constructs a new EmailAlreadyVerifiedException with the specified detail message.
     *
     * @param message the error message
     */
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
