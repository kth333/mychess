package com.g1.mychess.tournament.exception;

public class RequirementNotMetException extends RuntimeException {
    public RequirementNotMetException(String message) {
        super(message);
    }
}