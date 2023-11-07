package com.yoanpetrov.studentmanagementsystem.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.yoanpetrov.studentmanagementsystem.rest.controllers")
@Slf4j
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(Throwable ex) {
        log.debug("ResourceNotFoundException, returning 404. Message: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorBody("404", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<?> handleResourceConflictException(Throwable ex) {
        log.debug("ResourceConflictException, returning 409. Message: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorBody("409", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Throwable ex) {
        log.debug("BadCredentialsException, returning 401. Message: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorBody("401", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        log.debug("MethodArgumentNotValidException, returning 400.");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ValidationErrorBody errorBody = ValidationErrorBody.builder()
            .status("400")
            .errorMessage("JSON validation error")
            .errors(errors).build();
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
