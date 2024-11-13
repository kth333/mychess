package com.g1.mychess.admin.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.g1.mychess.admin.exception.*;

/**
 * GlobalExceptionHandler is responsible for handling exceptions across the application.
 * It catches specific exceptions and returns the appropriate HTTP response with the relevant error message.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AdminNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the AdminNotFoundException that was thrown
     * @return a ResponseEntity containing the error message and HttpStatus.NOT_FOUND
     */
    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<?> handleAdminNotFoundException(AdminNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidBlacklistOperationException and returns a 400 Bad Request response.
     *
     * @param ex the InvalidBlacklistOperationException that was thrown
     * @return a ResponseEntity containing the error message and HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(InvalidBlacklistOperationException.class)
    public ResponseEntity<?> handleInvalidBlacklistOperationException(InvalidBlacklistOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request response.
     *
     * @param ex the IllegalArgumentException that was thrown
     * @return a ResponseEntity containing the error message and HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}