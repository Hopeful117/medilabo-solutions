package com.Medilabo_solutions.Patient_service.exception;

/**
 * Exception thrown when an invalid date is provided.
 */
public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
