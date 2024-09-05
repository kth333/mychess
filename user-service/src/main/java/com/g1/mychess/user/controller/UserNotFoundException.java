package com.g1.mychess.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    UserNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}
