package com.example.sessionservice.service.exceptions;

public class SessionIdInvalidException extends RuntimeException {

    public SessionIdInvalidException(final String message) {
        super(message);
    }

    //TODO 25/8/2020 Override the no argument constructor for the SessionIdInvalidException to log a personalised message

}