package com.taldate.backend.auth.exception;

public class ProfileNotFoundException extends RuntimeException{

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
