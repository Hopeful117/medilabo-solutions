package com.Medilabo_solutions.Frontend_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Custom error controller to handle errors and display a user-friendly error page.
 * This controller captures the error status code and message and passes them to the view.
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object message = request.getAttribute("jakarta.servlet.error.message");

        request.setAttribute("status", status);
        request.setAttribute("message", message);

        return "error";
    }
    }

