package com.g1.mychess.match.exception;


public class TournamentRoundNotFoundException extends RuntimeException{
    public TournamentRoundNotFoundException(String message) {
        super(message);
    }
}
