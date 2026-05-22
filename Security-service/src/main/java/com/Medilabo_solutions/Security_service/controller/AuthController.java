package com.Medilabo_solutions.Security_service.controller;

import com.Medilabo_solutions.Security_service.dto.LoginRequestDTO;
import com.Medilabo_solutions.Security_service.dto.LoginResponseDTO;
import com.Medilabo_solutions.Security_service.repository.UserRepository;
import com.Medilabo_solutions.Security_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
/**
 * Controller responsible for handling authentication requests.
 * Provides an endpoint for user login, which validates credentials and returns a JWT token upon successful authentication.
 */
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Handles user login requests.
     * Validates the provided username and password, and if successful, generates and returns a JWT token.
     *
     * @param loginDTO The login request containing the username and password.
     * @return A LoginResponseDTO containing the generated JWT token.
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getUsername());
    authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),loginDTO.getPassword()
            ));
 String token = jwtService.generateToken(loginDTO.getUsername());
 return new LoginResponseDTO(token);
}


}
