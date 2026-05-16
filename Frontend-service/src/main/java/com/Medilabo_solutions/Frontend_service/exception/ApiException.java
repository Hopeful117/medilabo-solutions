package com.Medilabo_solutions.Frontend_service.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int status;
    public ApiException(String message, int status) {
        super(message);
        this.status=status;
    }
}
