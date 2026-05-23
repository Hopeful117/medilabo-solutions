package com.Medilabo_solutions.Frontend_service.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class GetAuthHeaders {
    public  HttpHeaders getAuthHeaders(HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
