package com.inventory.service;

/**
 * Custom runtime exception thrown when product input validation fails.
 * Helps cleanly decouple validation errors from SQL database errors.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
