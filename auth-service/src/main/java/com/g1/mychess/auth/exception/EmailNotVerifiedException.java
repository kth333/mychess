package com.g1.mychess.auth.exception;

/**
 * Exception thrown when a user attempts to perform an action that requires email verification,
 * but the email is not yet verified.
 */
public class EmailNotVerifiedException extends RuntimeException {

    /**
     * Constructs a new EmailNotVerifiedException with the specified error message.
     *
     * @param message the error message
     */
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
