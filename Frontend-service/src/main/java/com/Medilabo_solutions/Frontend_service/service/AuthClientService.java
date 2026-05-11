package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthClientService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/v1/auth";

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(loginRequestDTO, headers);
        ResponseEntity<LoginResponseDTO> response =
               restTemplate.exchange( BASE_URL + "/login", HttpMethod.POST, entity, LoginResponseDTO.class);

        return response.getBody();
    }
}
