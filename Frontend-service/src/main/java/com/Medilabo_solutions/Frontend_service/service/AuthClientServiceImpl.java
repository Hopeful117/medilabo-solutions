package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.LoginResponseDTO;
import com.Medilabo_solutions.Frontend_service.helper.RestTemplateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsible for communicating with the Authentication API to authenticate users.
 * It sends login requests to the Auth API and processes the responses, handling any exceptions that may occur.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthClientService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/v1/auth";

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("Calling Auth API to authenticate user: {}", loginRequestDTO.getUsername());
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(loginRequestDTO, headers);
        try {
            ResponseEntity<LoginResponseDTO> response =
                    restTemplate.exchange(BASE_URL + "/login", HttpMethod.POST, entity, LoginResponseDTO.class);
            log.info("Successfully authenticated user: {}", loginRequestDTO.getUsername());
            return response.getBody();
        } catch(HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);

        }
    }
}
