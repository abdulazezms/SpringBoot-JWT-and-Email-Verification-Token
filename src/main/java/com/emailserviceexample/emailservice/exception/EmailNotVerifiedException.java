package com.emailserviceexample.emailservice.exception;

import java.io.Serial;

public class EmailNotVerifiedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public EmailNotVerifiedException() {
        super();
    }

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
