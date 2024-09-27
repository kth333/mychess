package com.g1.mychess.match.exception;

public class TournamentNotFoundException extends RuntimeException{
    public TournamentNotFoundException(String msg) {
        super(msg);
    }
}
