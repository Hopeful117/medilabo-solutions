package com.Medilabo_solutions.Security_service.controller;

import com.Medilabo_solutions.Security_service.dto.LoginDTO;
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

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
    authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),loginDTO.getPassword()
            ));
    return jwtService.generateToken(loginDTO.getUsername());
}


}
