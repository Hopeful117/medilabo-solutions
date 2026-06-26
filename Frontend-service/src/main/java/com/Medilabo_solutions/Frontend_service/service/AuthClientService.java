package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.LoginResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="SECURITY-SERVICE")
public interface AuthClientService {
    @RequestMapping(method = RequestMethod.POST,value="/auth/login")
    LoginResponseDTO authenticate(LoginRequestDTO loginRequest);

}
