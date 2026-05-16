package com.Medilabo_solutions.Patient_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Patient Service application.
 * Handles various exceptions and returns appropriate HTTP responses with error details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler  {

    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the ResourceNotFoundException thrown
     * @return a ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles DuplicateResourceException and returns a 409 Conflict response.
     *
     * @param ex the DuplicateResourceException thrown
     * @return a ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateResourceException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request response with validation errors.
     *
     * @param ex the MethodArgumentNotValidException thrown
     * @return a ResponseEntity containing validation error details and HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

      return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    /**
     * Handles InvalidDateException and returns a 400 Bad Request response.
     *
     * @param ex the InvalidDateException thrown
     * @return a ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Object> handleInvalidDate(InvalidDateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles all other exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the Exception thrown
     * @return a ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobal(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    /**
     * Helper method to build a standardized error response body.
     * @param status the HTTP status to return
     * @param message the error message to include in the response
     * @return a ResponseEntity containing the error details and HTTP status
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", message);

        return new ResponseEntity<>(body, status);
    }
}
