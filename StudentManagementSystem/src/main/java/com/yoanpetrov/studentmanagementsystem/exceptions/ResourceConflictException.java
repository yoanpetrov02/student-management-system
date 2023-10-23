package com.yoanpetrov.studentmanagementsystem.exceptions;

/**
 * An exception for cases where a 409 Conflict status code should be returned from a controller.
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}
