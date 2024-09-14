package com.g1.mychess.auth.exception;

public class PlayerServiceException extends RuntimeException {
    public PlayerServiceException(String message) {
        super(message);
    }
}