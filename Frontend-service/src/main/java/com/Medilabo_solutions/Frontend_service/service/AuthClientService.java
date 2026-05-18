package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.LoginResponseDTO;

public interface AuthClientService {
    LoginResponseDTO authenticate(LoginRequestDTO loginRequest);

}
