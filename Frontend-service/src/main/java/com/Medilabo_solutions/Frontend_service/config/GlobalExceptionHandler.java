package com.Medilabo_solutions.Frontend_service.config;

import com.Medilabo_solutions.Frontend_service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to catch ApiException and display a user-friendly error page.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public String handleApiException(ApiException ex, Model model) {
        log.error("API Exception: {}", ex.getMessage(), ex);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", ex.getStatus());

        return "error";
    }
}
