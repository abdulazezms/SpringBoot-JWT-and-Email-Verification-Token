package com.emailserviceexample.emailservice.exception;

import java.io.Serial;

public class RegistrationFailedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RegistrationFailedException() {
        super();
    }

    public RegistrationFailedException(String message) {
        super(message);
    }
}
