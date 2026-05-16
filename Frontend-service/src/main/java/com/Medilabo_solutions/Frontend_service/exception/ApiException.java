package com.Medilabo_solutions.Frontend_service.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
