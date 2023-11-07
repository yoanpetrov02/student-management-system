package com.yoanpetrov.studentmanagementsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.yoanpetrov.studentmanagementsystem.rest.controllers")
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(Throwable ex) {
        return new ResponseEntity<>(new ErrorBody("404", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<?> handleResourceConflictException(Throwable ex) {
        return new ResponseEntity<>(new ErrorBody("409", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Throwable ex) {
        return new ResponseEntity<>(new ErrorBody("401", "Wrong password"), HttpStatus.UNAUTHORIZED);
    }
}
