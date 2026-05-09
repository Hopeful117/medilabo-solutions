package com.Medilabo_solutions.Security_service.controller;

import com.Medilabo_solutions.Security_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Security_service.dto.LoginResponseDTO;
import com.Medilabo_solutions.Security_service.repository.UserRepository;
import com.Medilabo_solutions.Security_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
    authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),loginDTO.getPassword()
            ));
 String token = jwtService.generateToken(loginDTO.getUsername());
 return new LoginResponseDTO(token);
}


}
