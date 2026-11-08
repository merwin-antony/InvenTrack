package com.inventory.service;

/**
 * Exception thrown when authentication or login validation fails.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }
}
