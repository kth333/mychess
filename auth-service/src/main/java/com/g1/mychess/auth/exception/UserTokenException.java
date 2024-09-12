package com.g1.mychess.auth.exception;

public class UserTokenException extends RuntimeException {
    public UserTokenException(String message) {
        super(message);
    }
}