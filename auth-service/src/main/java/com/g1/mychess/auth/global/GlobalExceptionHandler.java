package com.g1.mychess.auth.global;

import com.g1.mychess.auth.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Handles various custom exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UserNotFoundException and returns a 404 NOT_FOUND response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidCredentialsException and returns a 401 UNAUTHORIZED response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles EmailNotVerifiedException and returns a 403 FORBIDDEN response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<String> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles EmailAlreadyVerifiedException and returns a 409 CONFLICT response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<String> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles EmailSendFailedException and returns a 500 INTERNAL_SERVER_ERROR response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<String> handleEmailSendFailedException(EmailSendFailedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles InvalidPasswordException and returns a 400 BAD_REQUEST response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidRoleException and returns a 400 BAD_REQUEST response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<String> handleInvalidRoleException(InvalidRoleException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserAlreadyExistsException and returns a 409 CONFLICT response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles PlayerServiceException and returns a 500 INTERNAL_SERVER_ERROR response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(PlayerServiceException.class)
    public ResponseEntity<String> handlePlayerServiceException(PlayerServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles UserTokenException and returns a 400 BAD_REQUEST response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and the corresponding HTTP status
     */
    @ExceptionHandler(UserTokenException.class)
    public ResponseEntity<String> handleVerificationTokenException(UserTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * A generic handler for unexpected exceptions. Returns a 500 INTERNAL_SERVER_ERROR response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a generic error message and the corresponding HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
