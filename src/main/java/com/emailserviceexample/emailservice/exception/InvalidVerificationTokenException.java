package com.emailserviceexample.emailservice.exception;

public class InvalidVerificationTokenException extends RuntimeException{
    public InvalidVerificationTokenException() {
        super();
    }

    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
