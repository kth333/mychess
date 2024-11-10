package com.g1.mychess.auth.exception;

/**
 * Exception thrown when an email fails to send due to some internal error.
 */
public class EmailSendFailedException extends RuntimeException {

    /**
     * Constructs a new EmailSendFailedException with the specified error message.
     *
     * @param message the error message
     */
    public EmailSendFailedException(String message) {
        super(message);
    }
}
