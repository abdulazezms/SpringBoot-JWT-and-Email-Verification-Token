package com.emailserviceexample.emailservice.exception;

import com.emailserviceexample.emailservice.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<ApiError> registrationFailed(RegistrationFailedException ex){
        ApiError apiError = ApiError
                .builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<ApiError> invalidVerificationToken(InvalidVerificationTokenException ex){
        ApiError apiError = ApiError
                .builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiError> notVerified(EmailNotVerifiedException ex){
        ApiError apiError = ApiError
                .builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> tokenExpired(TokenExpiredException ex){
        ApiError apiError = ApiError
                .builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
