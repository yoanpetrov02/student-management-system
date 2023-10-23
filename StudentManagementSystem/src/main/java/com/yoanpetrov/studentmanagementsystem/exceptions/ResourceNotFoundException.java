package com.yoanpetrov.studentmanagementsystem.exceptions;

/**
 * An exception for cases where a 404 Not Found status code should be returned from a controller.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
