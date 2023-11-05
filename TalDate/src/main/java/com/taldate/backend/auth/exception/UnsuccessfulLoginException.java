package com.taldate.backend.auth.exception;

public class UnsuccessfulLoginException extends RuntimeException {
    public UnsuccessfulLoginException(String message) {
        super(message);
    }
}