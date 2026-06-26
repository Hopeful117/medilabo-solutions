package com.Medilabo_solutions.Evaluation_Service.config;

import com.Medilabo_solutions.Evaluation_Service.exception.PatientNotFoundException;
import io.swagger.v3.oas.models.examples.Example;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Object> handlePatientNotFound(PatientNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobal(Exception ex){
        log.error("Unexpected error:{}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,"unexpected error occured");
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
